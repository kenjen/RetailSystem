package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import tableModels.UneditableTableModel;
import data.Json;
import data.Product;
import data.Supplier;

public class StockManagementPanel extends JSplitPane {

	private Object[][] arrayTableProducts;
	//private JButton btnCreateNewProduct;
	private JButton btnDeleteProduct;
	private JButton btnDiscountProduct;
	private JButton btnDisplayAllProducts;
	private JButton btnDisplayLowStock;
	private JButton btnDisplayDeletedStock;
	private JButton btnDisplayProducts;
	private JButton btnFlagForOrder;
	private JButton btnRestoreProduct;
	private JButton btnSaveAll;
	private JComboBox<String> comboSelectSupplier;
	private boolean creatingNewProduct = false;
	private int currentTableView = 1;
	private JPopupMenu menu;
	private ActionListener menuListener;
	private Timer notificationTimer;
	private boolean productLoaded = false;
	private JScrollPane scrollPane;
	private JTable table;
	private JTextField textCategory;
	private JTextField textDiscountedPrice;
	private JTextField textId;
	private JTextField textName;
	private JTextField textPrice;
	private JTextField textQuantity;
	private JTextField textSearchByName;
	private JTextField textThreshold;
	private JTextField txtCategory;
	private JTextField txtDiscountedAmount;
	private JTextField txtDiscountedPrice;
	private JTextField txtFlaggedForOrder;
	private JTextField txtId;
	private JTextField txtName;
	private JTextField txtNotification;
	private JTextField txtPrice;
	private JTextField txtQuantity;
	private JTextField txtSearchByName;
	private JTextField txtSupplier;
	private JTextField txtThreshold;

	public StockManagementPanel() {

		// Timer to display notification for a few seconds
		notificationTimer = new Timer(3000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNotification.setVisible(false);
				System.out.println("TimerEnded");
				notificationTimer.stop();
			}
		});

		// Add scroll pane to left size with list of products
		scrollPane = new JScrollPane(table);
		this.setDividerLocation(300);
		setLeftComponent(scrollPane);
		Dimension min = new Dimension(300, 150);
		leftComponent.setMinimumSize(min);

		// add panel to right hand side
		JPanel panel = new JPanel();
		setRightComponent(panel);
		panel.setLayout(new MigLayout(
				"",
				"[][170px:n,grow][grow][70px:n,grow][::30px][100px:n,grow][100px:n,grow]",
				"[][][][][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][grow][][30px:n,grow]"));
		min = new Dimension(500, 150);
		rightComponent.setMinimumSize(min);

		// setup right click popup menu
		menu = new JPopupMenu();
		menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.out.println("Popup menu item ["
						+ event.getActionCommand() + "] was pressed.");
				rightClick(event.getActionCommand());
			}
		};
		JMenuItem item;
		menu.add(item = new JMenuItem("Delete"));
		item.addActionListener(menuListener);
		menu.add(item = new JMenuItem("Discount"));
		item.addActionListener(menuListener);
		menu.add(item = new JMenuItem("Edit"));
		item.addActionListener(menuListener);
		menu.add(item = new JMenuItem("Flag For Order"));
		item.addActionListener(menuListener);
		menu.add(item = new JMenuItem("Restore"));
		item.addActionListener(menuListener);

		// display products button
		btnDisplayProducts = new JButton("Display Stock");
		panel.add(btnDisplayProducts, "cell 1 1, growx");
		btnDisplayProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentTableView = 1;
				displayProductsTable("");
			}
		});

		// Button to display products with stock levels below threshold
		btnDisplayLowStock = new JButton("Display Low Stock");
		panel.add(btnDisplayLowStock, "cell 1 2, growx");
		btnDisplayLowStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentTableView = 2;
				displayProductsTable("LOWSTOCK");
			}
		});

		// Button to display deleted products
		btnDisplayDeletedStock = new JButton("Display Deleted Stock");
		panel.add(btnDisplayDeletedStock, "cell 1 3, growx");
		btnDisplayDeletedStock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentTableView = 3;
				displayProductsTable("DELETED");
			}
		});

		// Button to display all products including deleted products
		btnDisplayAllProducts = new JButton("Display All Stock");
		panel.add(btnDisplayAllProducts, "cell 1 4, growx");
		btnDisplayAllProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentTableView = 4;
				displayProductsTable("EVERYTHING");
			}
		});

		// search products by name entry
		txtSearchByName = new JTextField();
		txtSearchByName.setEditable(false);
		txtSearchByName.setHorizontalAlignment(SwingConstants.CENTER);
		txtSearchByName.setText("Enter Name To Search");
		panel.add(txtSearchByName, "cell 3 1,growx");
		textSearchByName = new JTextField();
		panel.add(textSearchByName, "cell 5 1,growx");
		textSearchByName.setColumns(10);
		textSearchByName.setFocusTraversalKeysEnabled(false);
		textSearchByName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_ENTER) {// TODO
					boolean valid = loadProductDetails(
							textSearchByName.getText(), Shop.getProducts());
					if (valid) {
						textName.requestFocusInWindow();
						textSearchByName.setText("");
					} else {
						clearProductDetails();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// text displaying notifications for the user
		txtNotification = new JTextField();
		txtNotification.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtNotification.setForeground(Color.RED);
		txtNotification.setEditable(false);
		txtNotification.setVisible(false);
		txtNotification.setHorizontalAlignment(SwingConstants.CENTER);
		txtNotification.setText("Enter Notification");
		panel.add(txtNotification, "cell 3 2 3 1,growx");
		txtNotification.setColumns(10);

		// text displaying the flagged for order if true
		txtFlaggedForOrder = new JTextField();
		txtFlaggedForOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtFlaggedForOrder.setForeground(Color.RED);
		txtFlaggedForOrder.setEditable(false);
		txtFlaggedForOrder.setVisible(false);
		txtFlaggedForOrder.setHorizontalAlignment(SwingConstants.CENTER);
		txtFlaggedForOrder.setText("FLAGGED FOR ORDER");
		panel.add(txtFlaggedForOrder, "cell 3 3 3 1,growx");
		txtFlaggedForOrder.setColumns(10);

		// product id fields
		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setHorizontalAlignment(SwingConstants.CENTER);
		txtId.setText("Id");
		panel.add(txtId, "cell 3 4,growx");
		txtId.setColumns(10);
		textId = new JTextField();
		textId.setEditable(false);
		panel.add(textId, "cell 5 4,growx");
		textId.setColumns(10);
		textId.setFocusTraversalKeysEnabled(false);

		// product name fields
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setText("Name");
		panel.add(txtName, "cell 3 6,growx");
		txtName.setColumns(10);
		textName = new JTextField();
		panel.add(textName, "cell 5 6,growx");
		textName.setColumns(10);
		textName.setFocusTraversalKeysEnabled(false);
		textName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_TAB) {
					textCategory.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// product category fields
		txtCategory = new JTextField();
		txtCategory.setHorizontalAlignment(SwingConstants.CENTER);
		txtCategory.setEditable(false);
		txtCategory.setText("Category");
		panel.add(txtCategory, "cell 3 8,growx");
		txtCategory.setColumns(10);
		textCategory = new JTextField();
		panel.add(textCategory, "cell 5 8,growx");
		textCategory.setColumns(10);
		textCategory.setFocusTraversalKeysEnabled(false);
		textCategory.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_TAB) {
					textQuantity.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// product quantity fields
		txtQuantity = new JTextField();
		txtQuantity.setText("Quantity");
		txtQuantity.setEditable(false);
		txtQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtQuantity, "cell 3 10,growx");
		txtQuantity.setColumns(10);
		textQuantity = new JTextField();
		panel.add(textQuantity, "cell 5 10,growx");
		textQuantity.setColumns(10);
		textQuantity.setFocusTraversalKeysEnabled(false);
		textQuantity.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_TAB) {
					textThreshold.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// product threshold fields
		txtThreshold = new JTextField();
		txtThreshold.setText("Order Threshold");
		txtThreshold.setEditable(false);
		txtThreshold.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(txtThreshold, "cell 3 12,growx");
		txtThreshold.setColumns(10);
		textThreshold = new JTextField();
		panel.add(textThreshold, "cell 5 12,growx");
		textThreshold.setColumns(10);
		textThreshold.setFocusTraversalKeysEnabled(false);
		textThreshold.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_TAB) {
					textPrice.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// product price fields
		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtPrice.setEditable(false);
		txtPrice.setText("Price");
		panel.add(txtPrice, "cell 3 14,growx");
		txtPrice.setColumns(10);
		textPrice = new JTextField();
		panel.add(textPrice, "cell 5 14,growx");
		textPrice.setColumns(10);
		textPrice.setFocusTraversalKeysEnabled(false);
		textPrice.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_TAB) {
					comboSelectSupplier.requestFocusInWindow();
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		});

		// product discounted price fields
		txtDiscountedPrice = new JTextField();
		txtDiscountedPrice.setHorizontalAlignment(SwingConstants.CENTER);
		txtDiscountedPrice.setEditable(false);
		txtDiscountedPrice.setText("Discounted Price");
		panel.add(txtDiscountedPrice, "cell 3 16,growx");
		txtDiscountedPrice.setColumns(10);
		textDiscountedPrice = new JTextField();
		panel.add(textDiscountedPrice, "cell 5 16,growx");
		textDiscountedPrice.setColumns(10);
		txtDiscountedAmount = new JTextField();
		txtDiscountedAmount.setHorizontalAlignment(SwingConstants.CENTER);
		txtDiscountedAmount.setEditable(false);
		txtDiscountedAmount.setText("");
		panel.add(txtDiscountedAmount, "cell 6 16");
		txtDiscountedAmount.setColumns(5);

		// product supplier fields
		txtSupplier = new JTextField();
		txtSupplier.setHorizontalAlignment(SwingConstants.CENTER);
		txtSupplier.setEditable(false);
		txtSupplier.setText("Supplier");
		panel.add(txtSupplier, "cell 3 18,growx");
		txtSupplier.setColumns(10);
		ArrayList<String> supplierNames = new ArrayList<String>();
		supplierNames.add("");
		for (Supplier supplier : Shop.getSuppliers()) {
			String name = supplier.getSupplierName();
			supplierNames.add(name);
		}
		comboSelectSupplier = new JComboBox(supplierNames.toArray());
		panel.add(comboSelectSupplier, "cell 5 18");
		comboSelectSupplier.setEditable(true);
		AutoCompleteDecorator.decorate(comboSelectSupplier);

		
		//save product button
		btnSaveAll = new JButton("Save");
		panel.add(btnSaveAll, "cell 5 20, growx, center");
		btnSaveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAll();
			}
		});

		// create new product button
		/*btnCreateNewProduct = new JButton("New Product");
		panel.add(btnCreateNewProduct, "cell 5 20, growx, center");
		btnCreateNewProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product product = createNewProduct(false);
				if (product != null) {
					Shop.getProducts().add(product);
					refreshTable();
				}
				saveDetails();
			}
		});*/

		// discount product by percentage
		btnDiscountProduct = new JButton("Discount");
		panel.add(btnDiscountProduct, "cell 5 22, growx, center");
		btnDiscountProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (productLoaded) {
					int id = 0;
					try {
						id = Integer.parseInt(textId.getText());
					} catch (ClassCastException e) {
						System.out.println("textId contains invaid string");
					}
					discountProduct(id, Shop.getProducts(), false);
					loadProductDetails(id, Shop.getProducts());
				}
			}
		});

		// Delete product button
		/*btnDeleteProduct = new JButton("Delete...");
		panel.add(btnDeleteProduct, "cell 5 22, growx, center");
		btnDeleteProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				try {
					id = Integer.parseInt(textId.getText());
				} catch (ClassCastException e) {
					System.out.println("textId contains invaid string");
				}
				deleteProduct(id, Shop.getProducts(), false);
				clearProductDetails();
				saveDetails();
			}
		});*/

		// mark product as low stock and needing orders
		btnFlagForOrder = new JButton("Flag For Order");
		panel.add(btnFlagForOrder, "cell 5 24, growx, center");
		btnFlagForOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int id = 0;
				try {
					id = Integer.parseInt(textId.getText());
				} catch (ClassCastException e) {
					System.out.println("textId contains invaid string");
				}
				flagForOrder(id, Shop.getProducts());
				saveAll();
			}
		});

		// Restore product button
		/*btnRestoreProduct = new JButton("Restore...");
		panel.add(btnRestoreProduct, "cell 5 24, growx, center");
		btnRestoreProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String tempId = JOptionPane
							.showInputDialog("Enter id of product to restore");
					int id = 0;
					try {
						id = Integer.parseInt(tempId);
					} catch (ClassCastException e) {
						System.out.println("tempId contains invaid string");
					}
					restoreProduct(id, Shop.getProducts());
				} catch (NumberFormatException nfe) {
					System.out.println("number entered not an integer");
					txtNotification.setText("Invalid id entered");
					notificationTimer.stop();
					txtNotification.setVisible(true);
					notificationTimer.start();
				}
				saveDetails();
			}
		});*/

		displayProductsTable("");
		scrollPane.getViewport().add(table);
	}

	// checks all fields valid
	public boolean allFieldsValid() {
		if (productLoaded) {
			try {
				int id = Integer.parseInt(textId.getText());
				for (Product product : Shop.getProducts()) {
					if (product.getId() == id) {

					} else {
						txtNotification.setText("No product with id " + id
								+ " found");
						notificationTimer.stop();
						txtNotification.setVisible(true);
						notificationTimer.start();
						return false;
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("textId contains invaid string");
				txtNotification.setText("Invalid id loaded");
				notificationTimer.stop();
				txtNotification.setVisible(true);
				notificationTimer.start();
				return false;
			}
		}

		if (!(textCategory.getText().trim().equals(""))) {
			if (!(textName.getText().trim().equals(""))) {
				if (!(textPrice.getText().trim().equals(""))) {
					try {
						Double.parseDouble(textPrice.getText().trim());
						if (!(textThreshold.getText().trim().equals(""))) {
							try {
								Integer.parseInt(textThreshold.getText().trim());
								if (!(textQuantity.getText().trim().equals(""))) {
									try {
										Integer.parseInt(textQuantity.getText().trim());
										if (!(((String) comboSelectSupplier.getSelectedItem()).trim().equals(""))) {
											return true;
										} else {
											txtNotification
													.setText("Empty Supplier");
											notificationTimer.stop();
											txtNotification.setVisible(true);
											notificationTimer.start();
											return false;
										}
									} catch (NumberFormatException e) {
										txtNotification
												.setText("Invalid Quantity Entered");
										notificationTimer.stop();
										txtNotification.setVisible(true);
										notificationTimer.start();
										return false;
									}
								} else {
									txtNotification.setText("Empty Quantity");
									notificationTimer.stop();
									txtNotification.setVisible(true);
									notificationTimer.start();
									return false;
								}
							} catch (NumberFormatException e) {
								txtNotification
										.setText("Invalid Threshold Entered");
								notificationTimer.stop();
								txtNotification.setVisible(true);
								notificationTimer.start();
								return false;
							}
						} else {
							txtNotification.setText("Empty Threshold");
							notificationTimer.stop();
							txtNotification.setVisible(true);
							notificationTimer.start();
							return false;
						}
					} catch (NumberFormatException e) {
						txtNotification.setText("Invalid Price Entered");
						notificationTimer.stop();
						txtNotification.setVisible(true);
						notificationTimer.start();
						return false;
					}
				} else {
					txtNotification.setText("Empty Price");
					notificationTimer.stop();
					txtNotification.setVisible(true);
					notificationTimer.start();
					return false;
				}
			} else {
				txtNotification.setText("Empty Name");
				notificationTimer.stop();
				txtNotification.setVisible(true);
				notificationTimer.start();
				return false;
			}
		} else {
			txtNotification.setText("Empty Category");
			notificationTimer.stop();
			txtNotification.setVisible(true);
			notificationTimer.start();
			return false;
		}
	}

	// Clears the right pane of any product details
	public void clearProductDetails() {
		textId.setText("");
		textName.setText("");
		textCategory.setText("");
		textQuantity.setText("");
		textThreshold.setText("");
		textPrice.setText("");
		txtDiscountedAmount.setText("");
		textDiscountedPrice.setText("");
		txtFlaggedForOrder.setVisible(false);
		comboSelectSupplier.setSelectedIndex(0);
	}

	public Product createNewProduct(boolean testing) {
		/*
		 * if(creatingNewProduct){
		 * txtNotification.setText("Currently Creating New Product");
		 * notificationTimer.stop(); txtNotification.setVisible(true);
		 * notificationTimer.start(); }else
		 */
		if (productLoaded) {
			// clear fields to add new details
			productLoaded = false;
			creatingNewProduct = true;
			clearProductDetails();
			textName.requestFocus();
			txtNotification.setText("Enter New Products Details");
			notificationTimer.stop();
			txtNotification.setVisible(true);
			notificationTimer.start();
			return null;
		} else {
			// take in new details and create new product
			if (!textName.getText().equals("")) {
				if (!(textCategory.getText().equals(""))) {
					try {
						Integer.parseInt(textQuantity.getText());
						try {
							Double.parseDouble(textPrice.getText());
							try {
								Integer.parseInt(textThreshold.getText());
								if (!testing) {
									if (comboSelectSupplier.getSelectedIndex() > 0) {
										for (Supplier supplier : Shop
												.getSuppliers()) {
											if (supplier
													.getSupplierName()
													.equals((String) comboSelectSupplier
															.getSelectedItem())) {
												boolean alreadyExists = false;
												for (Product product : Shop
														.getProducts()) {
													if (product
															.getName()
															.equalsIgnoreCase(
																	textName.getText())) {
														System.out
																.println("Product already exists");
														if (product
																.getSupplier()
																.getSupplierId() == supplier
																.getSupplierId()) {
															alreadyExists = true;
															System.out
																	.println("Supplier the same so product not added");
															txtNotification
																	.setText("Product Exists With Same Supplier");
															notificationTimer
																	.stop();
															txtNotification
																	.setVisible(true);
															notificationTimer
																	.start();
															break;
														}
													}
												}
												if (alreadyExists == false) {
													Product productToAdd = new Product(
															textName.getText(),
															textCategory
																	.getText(),
															Integer.parseInt(textQuantity
																	.getText()),
															Double.parseDouble(textPrice
																	.getText()),
															supplier, true, 20);
													productToAdd
															.setLowStockOrder(Integer
																	.parseInt(textThreshold
																			.getText()));
													clearProductDetails();
													System.out
															.println("Product Created Succesfully");
													return productToAdd;
												}
											}
										}
									} else {
										System.out
												.println("No supplier selected");
										txtNotification
												.setText("Please Select Supplier");
										notificationTimer.stop();
										txtNotification.setVisible(true);
										notificationTimer.start();
									}
								} else {
									Product product = new Product(
											textName.getText(),
											textCategory.getText(),
											Integer.parseInt(textQuantity
													.getText()),
											Double.parseDouble(textPrice
													.getText()),
											new Supplier(), true, 20);
									product.setLowStockOrder(Integer
											.parseInt(textThreshold.getText()));
									refreshTable();
									return product;
								}
							} catch (NumberFormatException nfe) {
								System.out
										.println("number format exception on threshold");
								txtNotification
										.setText("Please Enter Valid Threshold");
								notificationTimer.stop();
								txtNotification.setVisible(true);
								notificationTimer.start();
							}
						} catch (NumberFormatException nfe) {
							System.out
									.println("number format exception on price");
							txtNotification.setText("Please Enter Valid Price");
							notificationTimer.stop();
							txtNotification.setVisible(true);
							notificationTimer.start();
						}
					} catch (NumberFormatException nfe) {
						System.out
								.println("number format exception on quantity");
						txtNotification.setText("Please Enter Valid Quantity");
						notificationTimer.stop();
						txtNotification.setVisible(true);
						notificationTimer.start();
					}
				} else {
					System.out.println("Blank category");
					txtNotification.setText("Please Enter Valid Category");
					notificationTimer.stop();
					txtNotification.setVisible(true);
					notificationTimer.start();
				}
			} else {
				System.out.println("Blank Name");
				txtNotification.setText("Please Enter Valid Name");
				notificationTimer.stop();
				txtNotification.setVisible(true);
				notificationTimer.start();
			}
		}
		return null;
	}

	public void deleteProduct(int id, ArrayList<Product> products,
			boolean testing) {
		if (productLoaded) {
			Product remove = null;
			for (Product product : products) {
				if (product.getId() == id) {
					int selectedOption = 0;
					if (testing) {
						selectedOption = JOptionPane.YES_OPTION;
					} else {
						selectedOption = JOptionPane.showConfirmDialog(null,
								"Are you sure you wish to delete product?",
								"Warning", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
					}
					if (selectedOption == JOptionPane.YES_OPTION) {
						// check if user wants to mark as deleted or completly
						// remove record
						Object[] options = { "Mark", "Remove" };
						int markAsDeleted;
						if (testing) {
							markAsDeleted = JOptionPane.NO_OPTION;
						} else {
							markAsDeleted = JOptionPane.showOptionDialog(null,
									"Mark as deleted or permanently remove?",
									"Warning", JOptionPane.YES_NO_OPTION,
									JOptionPane.WARNING_MESSAGE, null, // do not
																		// use a
																		// custom
																		// Icon
									options, // the titles of buttons
									options[0]); // default button title
						}
						if (markAsDeleted == JOptionPane.YES_OPTION) {
							product.setDeleted(true);
						} else {
							remove = product;
						}
					}
				}
			}

			if (remove != null) {
				products.remove(remove);
			}

			refreshTable();
		}
	}

	public void discountProduct(int id, ArrayList<Product> products,
			boolean testing) {
		for (Product product : products) {
			if (product.getId() == id) {
				String input = null;
				if (testing) {
					input = "33";
				} else {
					input = JOptionPane
							.showInputDialog("Enter percentage to discount \n"
									+ "Pevious discount "
									+ product.getDiscountedPercentage() + "%");
				}
				if (input != null) {
					try {
						double inputD = Double.parseDouble(input);
						String test = String.format("%.2f", inputD);
						Double percent = Double.parseDouble(test);
						product.setDiscountedPercentage(percent);
						if (percent == 0) {
							product.setDiscounted(false);
						} else {
							product.setDiscounted(true);
						}
						int tempId = 0;
						try {
							id = Integer.parseInt(textId.getText());
						} catch (ClassCastException e) {
							System.out.println("textId contains invaid string");
						}
						loadProductDetails(tempId, Shop.getProducts());
					} catch (NumberFormatException nfe) {
						System.out.println("Entered value not a valid number");
						txtNotification.setText("Entered Invalid Number");
						notificationTimer.stop();
						txtNotification.setVisible(true);
						notificationTimer.start();
					}
				}
			}
		}
	}

	public void displayProductsTable(String productName) {
		if (productName.equals("DELETED")) {
			// display table for deleted products
			int counter = 0;
			for (Product product : Shop.getProducts()) {
				if (product.isDeleted()) {
					counter++;
				}
			}
			arrayTableProducts = new Object[counter][4];
			counter = 0;
			for (Product product : Shop.getProducts()) {
				if (product.isDeleted()) {
					arrayTableProducts[counter][0] = product.getId();
					arrayTableProducts[counter][1] = product.getName();
					arrayTableProducts[counter][2] = product.getQuantity();
					arrayTableProducts[counter][3] = product.getLowStockOrder();
					counter++;
				}
			}
		} else if (productName.equals("EVERYTHING")) {
			// display table for products incl. deleted products
			arrayTableProducts = new Object[Shop.getProducts().size()][4];
			int counter = 0;
			for (Product product : Shop.getProducts()) {
				arrayTableProducts[counter][0] = product.getId();
				arrayTableProducts[counter][1] = product.getName();
				arrayTableProducts[counter][2] = product.getQuantity();
				arrayTableProducts[counter][3] = product.getLowStockOrder();
				counter++;
			}
		} else if (productName.equals("LOWSTOCK")) {
			// display table for products with stock levels below threshold
			int counter = 0;
			for (Product product : Shop.getProducts()) {
				if (!product.isDeleted()
						&& product.getLowStockOrder() >= product.getQuantity()) {
					counter++;
				}
			}
			arrayTableProducts = new Object[counter][4];
			counter = 0;
			for (Product product : Shop.getProducts()) {
				if (!product.isDeleted()
						&& product.getLowStockOrder() >= product.getQuantity()) {
					arrayTableProducts[counter][0] = product.getId();
					arrayTableProducts[counter][1] = product.getName();
					arrayTableProducts[counter][2] = product.getQuantity();
					arrayTableProducts[counter][3] = product.getLowStockOrder();
					counter++;
				}
			}
		} else {
			// display table for all products not deleted
			int counter = 0;
			for (Product product : Shop.getProducts()) {
				if (!product.isDeleted()) {
					counter++;
				}
			}
			arrayTableProducts = new Object[counter][4];
			counter = 0;
			for (Product product : Shop.getProducts()) {
				if (!product.isDeleted()) {
					arrayTableProducts[counter][0] = product.getId();
					arrayTableProducts[counter][1] = product.getName();
					arrayTableProducts[counter][2] = product.getQuantity();
					arrayTableProducts[counter][3] = product.getLowStockOrder();
					counter++;
				}
			}
		}

		String columnNames[] = { "Id", "Name", "Quantity", "Threshold" };
		UneditableTableModel productsTableModel = new UneditableTableModel(
				arrayTableProducts, columnNames);
		table = new JTable(productsTableModel);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						int row = table.getSelectedRow();
						table.requestFocus();
						table.changeSelection(row, 1, false, false);
					}

				});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int r = table.rowAtPoint(e.getPoint());
				if (r >= 0 && r < table.getRowCount()) {
					table.setRowSelectionInterval(r, r);
				} else {
					table.clearSelection();
				}

				int rowindex = table.getSelectedRow();
				if (rowindex < 0)
					return;
				if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
					menu = new JPopupMenu();
					int row = table.getSelectedRow();
					int id = (int) table.getValueAt(row, 0);
					System.out.println("" + id);
					JMenuItem item;
					for (Product product : Shop.getProducts()) {
						if (product.getId() == id) {
							System.out.println("" + id);
							if (product.isDeleted()) {
								menu.add(item = new JMenuItem("Restore"));
								item.addActionListener(menuListener);
							} else {
								menu.add(item = new JMenuItem("Delete"));
								item.addActionListener(menuListener);
							}
						}
					}
					menu.add(item = new JMenuItem("Discount"));
					item.addActionListener(menuListener);
					menu.add(item = new JMenuItem("Edit"));
					item.addActionListener(menuListener);
					menu.add(item = new JMenuItem("Flag For Order"));
					item.addActionListener(menuListener);
					menu.add(item = new JMenuItem("New Product"));
					item.addActionListener(menuListener);
					menu.show(e.getComponent(), e.getX(), e.getY());

				} else if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					int id = (int) table.getValueAt(row, 0);
					loadProductDetails(id, Shop.getProducts());
				}
			}
		});

		scrollPane.getViewport().add(table);
	}

	public void flagForOrder(int id, ArrayList<Product> products) {
		if (productLoaded) {
			for (Product product : products) {
				if (product.getId() == id) {
					if (product.isFlaggedForOrder()) {
						product.setFlaggedForOrder(false);
						txtFlaggedForOrder.setVisible(false);
					} else {
						product.setFlaggedForOrder(true);
						txtFlaggedForOrder.setVisible(true);
					}
				}
			}
		}
	}

	public String getDisplayedName() {
		return textName.getText();
	}

	public String getDisplayedCategory() {
		return textCategory.getText();
	}

	public String getDisplayedQuantity() {
		return textQuantity.getText();
	}

	public String getDisplayedThreshold() {
		return textThreshold.getText();
	}

	public String getDisplayedPrice() {
		return textPrice.getText();
	}

	public String getDisplayedDiscountPrice() {
		return textDiscountedPrice.getText();
	}

	public String getDisplayedDiscountPercent() {
		return txtDiscountedAmount.getText();
	}

	public boolean isFlaggedForOrderVisible() {
		return txtFlaggedForOrder.isVisible();
	}

	public void loadProductDetails(int id, ArrayList<Product> products) {
		Product tempProduct = null;
		boolean productExists = false;
		for (Product product : products) {
			if (product.getId() == id) {
				productExists = true;
				tempProduct = product;
				productLoaded = true;
				break;
			}
		}
		if (!productExists) {
			System.out.println("*****This Id Does Not Match A Product*****");
			txtNotification.setText("Id Does Not Match A Product");
			notificationTimer.stop();
			txtNotification.setVisible(true);
			notificationTimer.start();
		} else {
			textId.setText("" + id);
			textName.setText(tempProduct.getName());
			textCategory.setText(tempProduct.getCategory());
			textQuantity.setText("" + tempProduct.getQuantity());
			textThreshold.setText("" + tempProduct.getLowStockOrder());
			textPrice.setText("" + tempProduct.getPrice());
			Double discountPrice = tempProduct.getPrice()
					- (tempProduct.getPrice() * (tempProduct
							.getDiscountedPercentage() / 100));
			DecimalFormat df = new DecimalFormat("#.##");
			textDiscountedPrice.setText("" + (df.format(discountPrice)));
			txtFlaggedForOrder.setVisible(tempProduct.isFlaggedForOrder());
			txtDiscountedAmount.setText(tempProduct.getDiscountedPercentage()
					+ "%");
			int index = 0;

			for (Supplier supplier : Shop.getSuppliers()) {
				index++;
				if (supplier.getSupplierName().equals(
						tempProduct.getSupplier().getSupplierName())) {
					comboSelectSupplier.setSelectedIndex(index);
				}
			}
		}
	}

	public boolean loadProductDetails(String name, ArrayList<Product> products) {
		Product tempProduct = null;
		boolean productExists = false;
		for (Product product : products) {
			if (product.getName().equalsIgnoreCase(name)) {
				productExists = true;
				tempProduct = product;
				productLoaded = true;
				break;
			}
		}
		if (!productExists) {
			txtNotification.setText(name + " Does Not Match A Product");
			notificationTimer.stop();
			txtNotification.setVisible(true);
			notificationTimer.start();
			return false;
		} else {
			textId.setText("" + tempProduct.getId());
			textName.setText(tempProduct.getName());
			textCategory.setText(tempProduct.getCategory());
			textQuantity.setText("" + tempProduct.getQuantity());
			textThreshold.setText("" + tempProduct.getLowStockOrder());
			textPrice.setText("" + tempProduct.getPrice());
			Double discountPrice = tempProduct.getPrice()
					- (tempProduct.getPrice() * (tempProduct
							.getDiscountedPercentage() / 100));
			DecimalFormat df = new DecimalFormat("#.##");
			textDiscountedPrice.setText("" + (df.format(discountPrice)));
			txtFlaggedForOrder.setVisible(tempProduct.isFlaggedForOrder());
			txtDiscountedAmount.setText(tempProduct.getDiscountedPercentage()
					+ "%");
			int index = 0;

			for (Supplier supplier : Shop.getSuppliers()) {
				index++;
				if (supplier.getSupplierName().equals(
						tempProduct.getSupplier().getSupplierName())) {
					comboSelectSupplier.setSelectedIndex(index);
				}
			}
			return true;
		}
	}

	// Check name and supplier against products
	public boolean productAndSupplierAlreadyExist(int id, String productName,
			String supplierName, ArrayList<Product> products,
			ArrayList<Supplier> suppliers) {
		for (Product product : products) {
			if (product.getName().equalsIgnoreCase(productName)) {
				if (product.getId() != id) {
					for (Supplier supplier : suppliers) {
						if (product.getSupplier() == supplier
								&& supplier.getSupplierName().equalsIgnoreCase(
										supplierName)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public void refreshTable() {
		if (currentTableView == 1) {
			displayProductsTable("");
		} else if (currentTableView == 2) {
			displayProductsTable("LOWSTOCK");
		} else if (currentTableView == 3) {
			displayProductsTable("DELETED");
		} else if (currentTableView == 4) {
			displayProductsTable("EVERYTHING");
		}
	}

	public boolean restoreProduct(int id, ArrayList<Product> products) {
		for (Product product : products) {
			if (product.getId() == id) {
				if (product.isDeleted()) {
					product.setDeleted(false);
					refreshTable();
					txtNotification.setText("Product Restored");
					notificationTimer.stop();
					txtNotification.setVisible(true);
					notificationTimer.start();
					return true;
				} else {
					txtNotification.setText("Product Is Already Available");
					notificationTimer.stop();
					txtNotification.setVisible(true);
					notificationTimer.start();
					return false;
				}
			}
		}
		txtNotification.setText("No Product With Matching Id Found");
		notificationTimer.stop();
		txtNotification.setVisible(true);
		notificationTimer.start();
		return false;
	}

	// method to perform right click actions
	public void rightClick(String command) {
		int row = table.getSelectedRow();
		int id = (int) table.getValueAt(row, 0);
		int deleteId = 0;
		for (Product product : Shop.getProducts()) {
			if (product.getId() == id) {
				loadProductDetails(id, Shop.getProducts());
				if (command.equals("Delete")) {
					deleteId = id;
				} else if (command.equals("Discount")) {
					discountProduct(id, Shop.getProducts(), false);
				} else if (command.equals("Edit")) {
					loadProductDetails(id, Shop.getProducts());
				} else if (command.equals("Flag For Order")) {
					flagForOrder(id, Shop.getProducts());
				} else if (command.equals("Restore")) {
					restoreProduct(id, Shop.getProducts());
				} else if (command.equals("New Product")) {
					setProductLoaded(true);
					createNewProduct(false);
				}
			}
		}
		if (deleteId != 0) {
			deleteProduct(id, Shop.getProducts(), false);
			clearProductDetails();
		}
		refreshTable();
	}

	public void saveAll() {
		if (creatingNewProduct) {
			System.out.println("Starting save new product");
			// TODO
			if (allFieldsValid()) {
				for (Supplier supplier : Shop.getSuppliers()) {
					if (supplier.getSupplierName().equals(
							(String) comboSelectSupplier.getSelectedItem())) {
						boolean alreadyExists = false;
						for (Product product : Shop.getProducts()) {
							if (product.getName().equalsIgnoreCase(
									textName.getText())) {
								System.out.println("Product already exists");
								if (product.getSupplier().getSupplierId() == supplier
										.getSupplierId()) {
									alreadyExists = true;
									System.out
											.println("Supplier the same so product not added");
									txtNotification
											.setText("Product Exists With Same Supplier");
									notificationTimer.stop();
									txtNotification.setVisible(true);
									notificationTimer.start();
									break;
								}
							}
						}
						if (alreadyExists == false) {
							Product productToAdd = new Product(
									textName.getText(), textCategory.getText(),
									Integer.parseInt(textQuantity.getText()),
									Double.parseDouble(textPrice.getText()),
									supplier, true, 20);
							productToAdd.setLowStockOrder(Integer
									.parseInt(textThreshold.getText()));
							clearProductDetails();
							System.out.println("Product Created Succesfully");
							Shop.getProducts().add(productToAdd);
							txtNotification.setText("New Product Added");
							notificationTimer.stop();
							txtNotification.setVisible(true);
							notificationTimer.start();
							refreshTable();
							creatingNewProduct = false;
							productLoaded = false;
						}
					}
				}
			}
		} else if (!creatingNewProduct) {
			int id = 0;
			try {
				id = Integer.parseInt(textId.getText());
			} catch (NumberFormatException e) {
				System.out.println("textId contains invaid string");
			}
			boolean notValid = productAndSupplierAlreadyExist(
					Integer.parseInt(textId.getText()), textName.getText(),
					(String) comboSelectSupplier.getSelectedItem(),
					Shop.getProducts(), Shop.getSuppliers());
			if (notValid) {
				System.out.println("Already Exists, Product Not Saved");
				txtNotification
						.setText("Product With Same Supplier Already Exists");
				notificationTimer.stop();
				txtNotification.setVisible(true);
				notificationTimer.start();
			} else if (allFieldsValid()) {
				for (Supplier supplier : Shop.getSuppliers()) {
					if (supplier.getSupplierName().equals(
							(String) comboSelectSupplier.getSelectedItem())) {
						for (Product product : Shop.getProducts()) {
							if (product.getId() == id) {
								product.setCategory(textCategory.getText()
										.trim());
								product.setName(textName.getText());
								double price = Double.parseDouble(textPrice
										.getText());
								String strPrice = String.format("%.2f", price);
								price = Double.parseDouble(strPrice);
								product.setPrice(price);
								product.setSupplier(supplier);
								int threshold = Integer.parseInt(textThreshold
										.getText().trim());
								product.setLowStockOrder(threshold);
								int quantity = Integer.parseInt(textQuantity
										.getText().trim());
								product.setQuantity(quantity);
							}
						}
					}
				}
				saveDetails();
				textId.setText("" + id);
				refreshTable();
				txtNotification.setText("Saved");
				notificationTimer.stop();
				txtNotification.setVisible(true);
				notificationTimer.start();
			}
		}
	}

	public void saveDetails() {

		Json.clearList("resources/products.json");
		for (Product product : Shop.getProducts()) {
			Json.saveProductToFile(product);
		}
		System.out.println("Finished saving products");
	}

	public void setDisplayedCategory(String str) {
		textCategory.setText(str);
	}

	public void setDisplayedDiscountPercent(String str) {
		txtDiscountedAmount.setText(str);
	}

	public void setDisplayedDiscountPrice(String str) {
		textDiscountedPrice.setText(str);
	}

	public void setDisplayedName(String str) {
		textName.setText(str);
	}

	public void setDisplayedPrice(String str) {
		textPrice.setText(str);
	}

	public void setDisplayedQuantity(String str) {
		textQuantity.setText(str);
	}

	public void setDisplayedThreshold(String str) {
		textThreshold.setText(str);
	}

	public void setProductLoaded(boolean productLoaded) {
		this.productLoaded = productLoaded;
	}
}