package notepadApp;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

class Notepadv1_2 {

    public static void main(String[] args) {
        // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        // MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        // MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            LOGGER.log(Level.INFO, "Ups");
        }
        SwingUtilities.invokeLater(MainFrame312::new);
    }
}

class MainFrame312 extends JFrame implements ActionListener, KeyListener, MouseListener, DocumentListener,
        ChangeListener, ItemListener, UndoableEditListener, Serializable {
    private final JTextArea ta;
    private final JPopupMenu pm;
    private final JMenuItem cut, copy, paste, delete, selectAll;
    private final Clipboard clp;
    private final PrinterJob pJ;
    private final JLabel statusL;
    private final UndoManager um = new UndoManager();
    private JMenuItem cut1, new1, open, save, saveAs, pageSetup, print, exit, undo1, copy1, paste1, delete1, find,
            findNext, replace, goTo, selectAll1, timeDate, font1, viewHelp, aboutNotepad;
    private JCheckBoxMenuItem wordW, statusBar;
    private JFileChooser fc = null;
    private boolean fileM = false;
    private PageFormat pF;
    private JLabel fontL;
    private JComboBox<?> fontCb, styleCb;
    private JSpinner sizeS;
    private String fontC = "Lucinda Console";
    private int styleC = Font.PLAIN, sizeC = 16, caretP = 0;
    private JFrame f;
    private JButton btn1, btn2, cancel1, replace1, replaceAll1;
    private JTextField tf, tf1, tf2;

    MainFrame312() {
        super("Window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(730, 520);

        String s = "/NotepadRes/Notepad.jpg";
        URL url = System.class.getResource(s);
        if (url == null) {
            System.err.println("Icon not find" + s);
        }
        Image icon = Toolkit.getDefaultToolkit().getImage(url);
        setIconImage(icon);
        ta = new JTextArea();
        pm = new JPopupMenu();
        setLayout(new BorderLayout());
        JScrollPane sp = new JScrollPane(ta);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(sp, BorderLayout.CENTER);
        setJMenuBar(menuBar());
        JMenuItem undo = new JMenuItem("Undo");
        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        paste = new JMenuItem("Paste");
        delete = new JMenuItem("Delete");
        selectAll = new JMenuItem("Select All              ");
        statusL = new JLabel("", JLabel.RIGHT);
        statusL.setFont(new Font("Arial", Font.PLAIN, 13));
        Dimension d = new Dimension(35, 26);
        statusL.setPreferredSize(d);
        add(statusL, BorderLayout.SOUTH);
        statusL.setVisible(false);
        updateL();
        pJ = PrinterJob.getPrinterJob();
        pF = pJ.defaultPage();
        ta.setFont(new Font("Arial", Font.PLAIN, 13));
        pm.add(undo);
        pm.addSeparator();
        pm.add(cut);
        pm.add(copy);
        pm.add(paste);
        pm.add(delete);
        pm.addSeparator();
        pm.add(selectAll);
        dad();
        clp = getToolkit().getSystemClipboard();
        ta.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    pm.show(ta, e.getX(), e.getY());
                }
            }
        });
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        paste1.addActionListener(this);
        delete.addActionListener(this);
        delete1.addActionListener(this);
        selectAll.addActionListener(this);
        selectAll1.addActionListener(this);
        ta.addKeyListener(this);
        ta.addMouseListener(this);
        pageSetup.addActionListener(this);
        cut1.addActionListener(this);
        copy1.addActionListener(this);
        aboutNotepad.addActionListener(this);
        exit.addActionListener(this);
        selectAll1.addActionListener(this);
        new1.addActionListener(this);
        open.addActionListener(this);
        statusBar.addActionListener(this);
        timeDate.addActionListener(this);
        viewHelp.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        wordW.addActionListener(this);
        font1.addActionListener(this);
        print.addActionListener(this);
        goTo.addActionListener(this);
        find.addActionListener(this);
        replace.addActionListener(this);
        findNext.addActionListener(this);
        ta.getDocument().addDocumentListener(this);
        ta.getDocument().addUndoableEditListener(this);
        undo1.addActionListener(this);
        setVisible(true);
    }

    private void dad() {
        // DropTarget dt =
        new DropTarget(ta, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent e) {
            }

            public void dragExit(DropTargetEvent e) {
            }

            public void dragOver(DropTargetDragEvent e) {
            }

            public void dropActionChanged(DropTargetDragEvent e) {
            }

            public void drop(DropTargetDropEvent e) {
                try {
                    // Accept the drop first, important!
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    // Get the files that are dropped as List
                    List<?> list = (List<?>) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    // Now get the first file from the list,
                    File file = (File) list.get(0);
                    ta.read(new FileReader(file), null);
                } catch (Exception ex) {
                    LOGGER.log(Level.INFO, "Ups");
                }
            }
        });
    }

    private JMenuBar menuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File  ");
        JMenu edit = new JMenu("Edit  ");
        JMenu format = new JMenu("Format  ");
        JMenu view = new JMenu("View  ");
        JMenu help = new JMenu("Help  ");
        new1 = new JMenuItem("New");
        open = new JMenuItem("Open...");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As...");
        pageSetup = new JMenuItem("Page Setup...");
        print = new JMenuItem("Print...");
        exit = new JMenuItem("Exit");
        undo1 = new JMenuItem("Undo");
        cut1 = new JMenuItem("Cut");
        copy1 = new JMenuItem("Copy");
        paste1 = new JMenuItem("Paste");
        delete1 = new JMenuItem("Delete");
        find = new JMenuItem("Find...");
        findNext = new JMenuItem("Find Next");
        replace = new JMenuItem("Replace...");
        goTo = new JMenuItem("Go To...");
        selectAll1 = new JMenuItem("Select All  ");
        timeDate = new JMenuItem("Time/Date      ");
        wordW = new JCheckBoxMenuItem("Word Wrap");
        font1 = new JMenuItem("Font...");
        wordW.setFont(new Font("Arial", Font.PLAIN, 12));
        statusBar = new JCheckBoxMenuItem("Status Bar   ");
        viewHelp = new JMenuItem("View Help  ");
        aboutNotepad = new JMenuItem("About Notepad");
        Font font = new Font("Arial", Font.PLAIN, 12);
        mb.add(file);
        mb.add(edit);
        mb.add(format);
        mb.add(view);
        mb.add(help);
        file.add(new1);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(pageSetup);
        file.add(print);
        file.addSeparator();
        file.add(exit);
        edit.add(undo1);
        edit.add(findNext);
        edit.addSeparator();
        edit.add(cut1);
        edit.add(copy1);
        edit.add(paste1);
        edit.add(delete1);
        edit.addSeparator();
        edit.add(find);
        edit.add(replace);
        edit.add(goTo);
        edit.addSeparator();
        edit.add(selectAll1);
        edit.add(timeDate);
        format.add(wordW);
        format.add(font1);
        view.add(statusBar);
        help.add(viewHelp);
        help.addSeparator();
        help.add(aboutNotepad);
        delete1.setFont(font);
        find.setFont(font);
        paste1.setFont(font);
        cut1.setFont(font);
        copy1.setFont(font);
        findNext.setFont(font);
        replace.setFont(font);
        goTo.setFont(font);
        selectAll1.setFont(font);
        timeDate.setFont(font);
        new1.setFont(font);
        new1.setIcon(new ImageIcon("new.png"));
        open.setFont(font);
        save.setFont(font);
        saveAs.setFont(font);
        pageSetup.setFont(font);
        print.setFont(font);
        exit.setFont(font);
        undo1.setFont(font);
        font1.setFont(font);
        statusBar.setFont(font);
        viewHelp.setFont(font);
        aboutNotepad.setFont(font);
        file.setFont(font);
        undo1.setFont(font);
        edit.setFont(font);
        format.setFont(font);
        view.setFont(font);
        help.setFont(font);
        file.setMnemonic(KeyEvent.VK_F);
        new1.setMnemonic(KeyEvent.VK_N);
        open.setMnemonic(KeyEvent.VK_O);
        save.setMnemonic(KeyEvent.VK_S);
        saveAs.setMnemonic(KeyEvent.VK_A);
        pageSetup.setMnemonic(KeyEvent.VK_U);
        print.setMnemonic(KeyEvent.VK_P);
        exit.setMnemonic(KeyEvent.VK_X);
        new1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        undo1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        cut1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        copy1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        paste1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        delete1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
        goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        selectAll1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        timeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        return mb;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        String s11;
        if ((src == cut) || (src == cut1)) {
            String s = ta.getSelectedText();
            StringSelection ss = new StringSelection(s);
            clp.setContents(ss, ss);
            ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
        } else if ((src == copy) || (src == copy1)) {
            String s = ta.getSelectedText();
            StringSelection ss = new StringSelection(s);
            clp.setContents(ss, ss);
        } else if ((src == paste) || (src == paste1)) {
            Transferable data = clp.getContents(MainFrame312.this);
            try {
                String s = (String) data.getTransferData(DataFlavor.stringFlavor);
                ta.replaceRange(s, ta.getSelectionStart(), ta.getSelectionEnd());
            } catch (UnsupportedFlavorException | IOException e1) {
                LOGGER.log(Level.INFO, "Ups");
            }
        } else if ((src == delete) || (src == delete1)) {
            ta.replaceRange("", ta.getSelectionStart(), ta.getSelectionEnd());
        } else if ((src == selectAll) || (src == selectAll1)) {
            ta.selectAll();
        } else if (src == aboutNotepad) {
            String s = "/NotepadRes/Notepad.jpg";
            URL url = System.class.getResource(s);
            assert url != null;
            ImageIcon icon = new ImageIcon(url);
            JOptionPane.showMessageDialog(MainFrame312.this, "Notepad v.1.2 \n " + "Development: \n " + "Stark C.\n ",
                    "About Notepad", JOptionPane.INFORMATION_MESSAGE, icon);

        } else if (src == pageSetup) {
            pF = pJ.pageDialog(pF);
        } else if (src == wordW) {
            ta.setLineWrap(wordW.getState());
            if (wordW.getState()) {
                statusBar.setEnabled(false);
                statusL.setVisible(false);
                goTo.setEnabled(false);
            } else {
                statusBar.setEnabled(true);
                goTo.setEnabled(true);
            }
        } else if (src == font1) {
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();

            JFrame f = new JFrame("Select Font");
            fontL = new JLabel("\nThis is a sample text");
            JPanel p = new JPanel();
            p.add(new JLabel("Font:"));
            fontCb = new JComboBox<Object>(gEnv.getAvailableFontFamilyNames());
            fontCb.setSelectedItem(fontC);
            fontCb.setMaximumRowCount(5);
            fontCb.addItemListener(this);
            p.add(fontCb);
            p.add(new JLabel("Style:"));
            String[] s = {"Plain", "Bold", "Italic", "Bold Italic"};
            styleCb = new JComboBox<Object>(s);
            styleCb.setSelectedItem(styleC);
            styleCb.addItemListener(this);
            p.add(styleCb);
            p.add(new JLabel("Size:"));
            sizeS = new JSpinner(new SpinnerNumberModel(sizeC, 6, 24, 1));
            sizeS.addChangeListener(this);
            p.add(sizeS);
            JPanel p2 = new JPanel();
            p2.add(fontL);
            f.setLayout(new BorderLayout());
            f.add(p, BorderLayout.NORTH);
            f.add(p2, BorderLayout.SOUTH);
            f.setLocation(100, 100);
            f.pack();
            f.setVisible(true);
        } else if (src == statusBar) {
            if (statusBar.getState()) {
                updateL();
                statusL.setVisible(true);
            } else {
                statusL.setVisible(false);
            }
        } else if (src == timeDate) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("E dd:MM:yyyy '-' hh:mm:ss a");
            String s = sdf.format(d);
            ta.replaceRange(s, ta.getSelectionStart(), ta.getSelectionEnd());
        } else if (src == viewHelp) {
            JFrame f = SingletonF1.getInstance();
            JTextPane tp = new JTextPane();
            tp.setEditable(false);
            JScrollPane sp = new JScrollPane(tp);
            String si = "/NotepadRes/Notepad.jpg";
            URL urli = System.class.getResource(si);
            Image i = Toolkit.getDefaultToolkit().getImage(urli);
            f.setIconImage(i);
            f.add(sp);
            f.setSize(500, 675);
            f.setLocation(900, 0);
            f.setResizable(false);
            f.setVisible(true);
            try {
                URL url = System.class.getResource("/NotepadRes/help.html");
                tp.setPage(url);
            } catch (Exception ee) {
                LOGGER.log(Level.INFO, "Ups");
            }
        } else if (src == print) {
            try {
                ta.print();
            } catch (PrinterException e1) {
                LOGGER.log(Level.INFO, "Ups");
            }
        } else if (src == goTo) {
            do {
                try {
                    String s = (String) JOptionPane.showInputDialog(MainFrame312.this, "Line number:\t", "Goto line",
                            JOptionPane.PLAIN_MESSAGE, null, null, null);
                    if (s == null) {
                        break;
                    }
                    int i1 = Integer.parseInt(s);
                    if (i1 > ta.getLineCount()) {
                        JOptionPane.showMessageDialog(MainFrame312.this,
                                "The line number is beyond the  total number of lines", "Notepad - Goto Line",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    for (int i = 0; i < ta.getLineCount(); i++) {
                        if (i + 1 == i1) {
                            ta.setCaretPosition(ta.getLineStartOffset(i));
                            return;
                        }
                    }
                } catch (Exception e2) {
                    LOGGER.log(Level.INFO, "Ups");
                }
            } while (true);
        } else if (src == find) {
            f = new JFrame("Find");
            String s = "/NotepadRes/Notepad.jpg";
            URL url = System.class.getResource(s);
            Image i = Toolkit.getDefaultToolkit().getImage(url);
            f.setIconImage(i);
            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            JLabel l = new JLabel("Find what:");
            tf = new JTextField(20);
            btn1 = new JButton("Find Next");
            btn2 = new JButton("Cancel");
            btn1.addActionListener(this);
            btn2.addActionListener(this);
            p1.add(l);
            p1.add(tf);
            p2.setLayout(new GridLayout(2, 1));
            p2.add(btn1);
            p2.add(btn2);
            f.setLayout(new BorderLayout());
            f.add(p1, BorderLayout.WEST);
            f.add(p2, BorderLayout.EAST);
            f.setLocation(100, 100);
            f.pack();
            f.setResizable(false);
            f.setVisible(true);
        } else if (btn2 == e.getSource()) {
            f.dispose();
        } else if (btn1 == e.getSource()) {
            String s2 = ta.getText();
            s11 = tf.getText();
            int i = s2.indexOf(s11, caretP);
            if (i >= 0) {
                caretP = i;
                ta.setCaretPosition(i);
            } else {
                JOptionPane.showMessageDialog(MainFrame312.this, "Cannot find string:  " + s11, "Notepad",
                        JOptionPane.PLAIN_MESSAGE);
            }
            f.dispose();
        } else if (replace == e.getSource()) {
            f = new JFrame("Replace");
            String s = "/NotepadRes/Notepad.jpg";
            URL url = System.class.getResource(s);
            Image i = Toolkit.getDefaultToolkit().getImage(url);
            f.setIconImage(i);
            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            JLabel l1 = new JLabel("Find what:");
            JLabel l2 = new JLabel("Replace with:");
            tf1 = new JTextField(10);
            tf2 = new JTextField(10);
            replace1 = new JButton("Replace");
            replaceAll1 = new JButton("Replace All");
            cancel1 = new JButton("Cancel");
            replace1.addActionListener(this);
            replaceAll1.addActionListener(this);
            cancel1.addActionListener(this);
            p1.setLayout(new GridLayout(3, 2));
            p1.add(l1);
            p1.add(tf1);
            p1.add(l2);
            p1.add(tf2);
            p2.setLayout(new GridLayout(3, 1));
            p2.add(replace1);
            p2.add(replaceAll1);
            p2.add(cancel1);
            f.setLayout(new BorderLayout());
            f.add(p1, BorderLayout.WEST);
            f.add(p2, BorderLayout.EAST);
            f.setLocation(100, 100);
            f.pack();
            f.setVisible(true);
        } else if (cancel1 == e.getSource()) {
            f.dispose();
        } else if (replace1 == e.getSource()) {
            String s1 = ta.getText();
            String s2 = tf1.getText();
            String s3 = tf2.getText();
            int i = s1.indexOf(s2);
            StringBuilder sb = new StringBuilder();
            if (i >= 0) {
                sb.append(s1, 0, i);
                sb.append(s3);
                sb.append(s1.substring(i + s2.length()));
                ta.setText(sb.toString());
            } else {
                JOptionPane.showMessageDialog(MainFrame312.this, "Cannot find string " + s2, "Notepad",
                        JOptionPane.PLAIN_MESSAGE);
            }
            f.dispose();
        } else if (replaceAll1 == e.getSource()) {
            String s1 = ta.getText();
            String s2 = tf1.getText();
            String s3 = tf2.getText();
            int i = s1.indexOf(s2);
            StringBuilder sb = new StringBuilder();
            if (i >= 0) {
                int s = 0;
                while ((i = s1.indexOf(s2, s)) >= 0) {
                    sb.append(s1, s, i);
                    sb.append(s3);
                    s = i + s2.length();
                }
                sb.append(s1.substring(s));
                ta.setText(sb.toString());
            } else {
                JOptionPane.showMessageDialog(MainFrame312.this, "Cannot find string " + s2, "Notepad",
                        JOptionPane.PLAIN_MESSAGE);
            }
            f.dispose();
        } else if (findNext == e.getSource()) {
            if (tf == null) {
                find.doClick();
            } else {
                String s2 = ta.getText();
                s11 = tf.getText();
                int i = s2.indexOf(s11, caretP + 1);
                if (i >= 0) {
                    caretP = i;
                    ta.setCaretPosition(i);
                } else {
                    JOptionPane.showMessageDialog(MainFrame312.this, "Cannot find string:  " + s11, "Notepad",
                            JOptionPane.PLAIN_MESSAGE);
                }
                f.dispose();
            }
        } else if (undo1 == e.getSource()) {
            if (um.canUndo()) {
                um.undo();
            }
        }
        String s = e.getActionCommand();
        label:
        {
            if (s.equals("New")) {
                newH();
                break label;
            }
            if (s.equals("Open...")) {
                openH();
                break label;
            }
            if (s.equals("Save As...")) {
                saveAsH();
                break label;
            }
            if (s.equals("Save")) {
                saveH();
                break label;
            }
            if (s.equals("Exit")) {
                exitH();
            }
        }
    }

    private void newH() {
        if (fileM) {
            String[] s = {"Save", "Don't Save", "Cancel"};
            int i = JOptionPane.showOptionDialog(MainFrame312.this, "Do you want to save changes?", "Confirm",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, s, s[0]);
            if (i == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (i == JOptionPane.YES_OPTION) {
                saveH();
                if (null == fc) {
                    return;
                }
            }
        }
        fc = null;
        ta.setText("");
        MainFrame312.this.setTitle("Untitled - Notepad");
        fileM = false;
    }

    private void openH() {
        if (fileM) {
            String[] s = {"Save", "Don't Save", "Cancel"};
            int i = JOptionPane.showOptionDialog(MainFrame312.this, "Do you want to save changes?", "Confirm",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, s, s[0]);
            if (i == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (i == JOptionPane.YES_OPTION) {
                saveH();
                if (null == fc) {
                    return;
                }
            }
            fileM = false;
        }
        fc = new JFileChooser();
        int i = fc.showOpenDialog(MainFrame312.this);

        if (i != JFileChooser.APPROVE_OPTION) {
            fc = null;
            return;
        }
        MainFrame312.this.setTitle(fc.getSelectedFile().getName() + " - Notepad");
        try {
            FileReader fr = new FileReader(fc.getSelectedFile());
            ta.read(fr, null);
            fr.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(MainFrame312.this, "Error opening file : " + fc.getSelectedFile().getName(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Ups");
        }
    }

    private void saveH() {
        if (fc == null) {
            saveAsH();
            return;
        }
        try {
            FileWriter fw = new FileWriter(fc.getSelectedFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String s = ta.getText();
            bw.write(s, 0, s.length());
            bw.close();
            fileM = false;
        } catch (IOException e) {
//            e.printStackTrace();
            LOGGER.log(Level.INFO, "Ups");
        }
    }

    private void saveAsH() {
        fc = new JFileChooser();
        int i = fc.showSaveDialog(MainFrame312.this);
        if (i != JFileChooser.APPROVE_OPTION) {
            fc = null;
            return;
        }
        if (fc.getSelectedFile().exists()) {
            String[] s = {"Yes", "No"};
            int i2 = JOptionPane.showOptionDialog(MainFrame312.this, "Overwrite existing file?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, s, s[1]);
            if (i2 != JOptionPane.YES_OPTION) {
                fc = null;
                return;
            }
        }
        MainFrame312.this.setTitle(fc.getSelectedFile().getName() + " - Notepad");
        try {
            FileWriter fw = new FileWriter(fc.getSelectedFile());
            BufferedWriter bw = new BufferedWriter(fw);
            String s = ta.getText();
            bw.write(s, 0, s.length());
            bw.close();
            fileM = false;
        } catch (IOException e) {
//            e.printStackTrace();
//            log.error("Ops!", e);
            LOGGER.log(Level.INFO, "Ups");
//            log.info("ups");
        }
    }

    private void exitH() {
        if (fileM) {
            String[] s = {"Save", "Don't Save", "Cancel"};
            int i = JOptionPane.showOptionDialog(MainFrame312.this, "Do you want to save changes?", "Confirm",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, s, s[0]);
            if (i == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (i == JOptionPane.YES_OPTION) {
                saveH();
                if (null == fc) {
                    return;
                }
            }
            fileM = false;
        }
        System.exit(0);
    }

    public void keyPressed(KeyEvent ke) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void keyReleased(KeyEvent ke) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void keyTyped(KeyEvent ae) {
        if (ae.getKeyChar() == KeyEvent.VK_ESCAPE) {
            f.dispose();
        }
        caretP = ta.getCaretPosition();
        updateL();
    }

    // MouseListener event handler
    public void mousePressed(MouseEvent e) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void mouseReleased(MouseEvent e) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void mouseEntered(MouseEvent e) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void mouseExited(MouseEvent e) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void mouseClicked(MouseEvent e) {
        caretP = ta.getCaretPosition();
        updateL();
    }

    // DocumentListener event handler
    public void changedUpdate(DocumentEvent de) {
        fileM = true;
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void insertUpdate(DocumentEvent de) {
        fileM = true;
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void removeUpdate(DocumentEvent de) {
        fileM = true;
        caretP = ta.getCaretPosition();
        updateL();
    }

    // UndoableEditListener event handler
    public void undoableEditHappened(UndoableEditEvent ue) {
        fileM = true;
        um.addEdit(ue.getEdit());
        caretP = ta.getCaretPosition();
        updateL();
    }

    public void stateChanged(ChangeEvent ce) {
        try {
            String size = sizeS.getModel().getValue().toString();
            sizeC = Integer.parseInt(size);
            ta.setFont(new Font(fontC, styleC, sizeC));
            fontL.setFont(new Font(fontC, styleC, sizeC));
        } catch (NumberFormatException nfe) {
            LOGGER.log(Level.INFO, "Ups");
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        label:
        {
            if (ie.getStateChange() != ItemEvent.SELECTED) {
                break label;
            }
            if (ie.getSource() == fontCb) {
                fontC = (String) fontCb.getSelectedItem();
            }
            if (ie.getSource() == styleCb) {
                styleC = styleCb.getSelectedIndex();
            }
            ta.setFont(new Font(fontC, styleC, sizeC));
            fontL.setFont(new Font(fontC, styleC, sizeC));
        }
    }

    private void updateL() {
        int i2 = ta.getCaretPosition();
        int i = ta.getLineCount();
        int col = 1, row = 1;
        String s = "Ln " + row + ", Col " + col;
        while (i > 0) {
            row = i--;
            try {
                if (i2 >= ta.getLineStartOffset(i)) {
                    col = i2 - ta.getLineStartOffset(i) + 1;
                    s = "Ln " + row + ", Col " + col + "   ";
                    break;
                }
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "Ups");
            }
        }
        statusL.setText(s);
    }
}

class SingletonF1 extends JFrame {
    private static SingletonF1 myInstance;

    private SingletonF1() {
    }

    public static SingletonF1 getInstance() {
        return ((myInstance == null) ? myInstance = new SingletonF1() : myInstance);
    }
}