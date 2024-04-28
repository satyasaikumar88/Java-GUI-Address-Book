import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class AddressBookGUI extends JFrame {

    private JTextField nameField, emailField, phoneField, searchField;
    private JButton addButton, deleteButton, clearAllButton;
    private JList<String> displayList;
    private DefaultListModel<String> listModel;
    private ArrayList<Contact> contacts;

    public AddressBookGUI() {
        super("Address Book");
        // Initialize contacts arraylist
        contacts = new ArrayList<>();
        // Create components
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addButton = new JButton("Add Contact");
        deleteButton = new JButton("Delete Contact");
        clearAllButton = new JButton("Clear All");
        displayList = new JList<>();
        listModel = new DefaultListModel<>();
        displayList.setModel(listModel);
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateList(searchField.getText());
            }
        });
        // Layout
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(new JLabel(""));  // Empty label for layout
        inputPanel.add(clearAllButton);
        JScrollPane scrollPane = new JScrollPane(displayList);
        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        // Add action listeners
        addButton.addActionListener(e -> addContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearAllButton.addActionListener(e -> clearAllContacts());
        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 380); // Adjust size for additional button
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
        // Load contacts from file
        loadContacts();
    }

    private void addContact() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        Contact contact = new Contact(name, email, phone);
        contacts.add(contact);
        listModel.addElement(contact.toString());
        // Clear fields
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        // Save contacts to file
        saveContacts();
    }

    private void deleteContact() {
        int index = displayList.getSelectedIndex();
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
            listModel.remove(index);
            // Save contacts to file
            saveContacts();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.");
        }
    }

    private void clearAllContacts() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all contacts?", "Clear All Contacts", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            contacts.clear();
            listModel.clear(); // Clear the listModel
            // Save contacts to file
            saveContacts();
        }
    }

    private void updateList(String searchText) {
        listModel.clear();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                contact.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                contact.getPhone().toLowerCase().contains(searchText.toLowerCase())) {
                listModel.addElement(contact.toString());
            }
        }
    }

    private void saveContacts() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("contacts.dat"))) {
            outputStream.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadContacts() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("contacts.dat"))) {
            contacts = (ArrayList<Contact>) inputStream.readObject();
            for (Contact contact : contacts) {
                listModel.addElement(contact.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddressBookGUI::new);
    }
}

class Contact implements Serializable {
    private String name;
    private String email;
    private String phone;

    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + " | " + email + " | " + phone;
    }
}



