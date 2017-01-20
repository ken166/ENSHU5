package kadai14.viewer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.UIManager;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import kadai14.data.CompareByDateSimilarity;
import kadai14.data.CompareByFileSize;

import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 写真ファイルを画面上に複数配置し、コメントを書き入れたり、
 * 指定日時に近い写真を前面にするようなソートをしたりできるアプリケーション
 *
 * @author i
 *
 */
public class ImageViewer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ImageViewManager manager = new ImageViewManager();
	public Point pos=new Point(); /*ドラッグしたとき基準となる座標*/
	
	/* 操作対象となる、主な GUI コンポーネント */
	private JTextField conditionField;  /* 写真ソート用の日時記入欄 */
	private JButton sortButton;         /* ソート開始用ボタン */
	private JTextField dateField;        /* 写真の日付属性表示用 */
	private JTextField sizeField;        /* 写真のサイズ情報表示用 */
	private ImagePanel imagePanel;		 /* 写真表示用 Panel */
	private JTextArea commentArea;	     /* コメント表示・編集用 JTextArea */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageViewer frame = new ImageViewer();
					frame.setup();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * マネージャー初期化用ルーチン
	 */
	public void setup() {
		File file=new File("manager.save");
		if(file.exists()){
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream("manager.save"));
				manager=(ImageViewManager)in.readObject();
				
				Iterator<ImageFileAnnotation> it=manager.list.iterator();
				while(it.hasNext()){
					it.next().setup();
				}
				select(manager.getSelected());
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}	
		}
		else{
		manager.gatherFiles(new File("image"), 300, 300);
		}
		imagePanel.setManager(manager);
		imagePanel.repaint();
	}


	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT); /* 日付情報の分析用 */

	/**
	 * 指定日時もしくはサイズによるソートを実行
	 * @throws ParseException 
	 */
	public void sort() throws ParseException {
		/*
		 * hint:
		 * ソート条件は、conditionField に記載
		 * 文字列から Date への変換は、
		 * df.parse("2014/4/3") -> "Wed Jun 11 00:00:00 JST 2014"相当の Date オブジェクト
		 * で可能。Date#getTime() で、ミリ秒情報(long型)が得られる
		 */
		String str=conditionField.getText();
		if(str.equals("size")){
			manager.sortBy(new CompareByFileSize());
		}
		else {
		Date date=df.parse(str);
		manager.sortBy(new CompareByDateSimilarity(date.getTime()));
		}
	}
	/**
	 * ドラッグ時の処理
	 * @param e
	 */
	public void drag(MouseEvent e){
		ImageFileAnnotation image;
		image=manager.getSelected();
		if(image!=null){
		if(image.contain(pos)){		//posが選択された画像の範囲のとき
		image.p.x=image.p.x+e.getX()-pos.x;
		image.p.y=image.p.y+e.getY()-pos.y;
		pos.x=e.getX();	//基準となる座標の更新
		pos.y=e.getY();	//基準となる座標の更新
		
		}
		}
	}
	
	/**
	 * ダブルクリック時の処理
	 * @param e
	 */
	public void doubleClick(MouseEvent e){
		select(imagePanel.clicked(e.getPoint()));
		ImageFileAnnotation image=manager.getSelected();
		manager.list.remove(image);	//ダブルクリックされたイメージの除去
		manager.list.add(image);	//ダブルクリックされたイメージをリストに新しく加える
	}
	
	/**
	 * ある ImageFileAnnotation が選択されたときの処理(サイズ属性やコメントなどの表示)
	 * @param item 選択を解除する場合は、item として null を与える
	 */
	public void select(ImageFileAnnotation item) {
		if(manager!=null) {
			manager.select(item);
		if(manager.getSelected()!=null){
			if(item!=null){
			dateField.setText(manager.getSelected().getDateStr());
			sizeField.setText(Long.toString(manager.getSelected().file.length()));	
			commentArea.setText(manager.getSelected().comment);
			}
			}	
		}
	}
	
	/**
	 * ImagePanel 上にマウスクリックイベントが起きたときの処理
	 * @param e ImagePanel 上の MouseEvent (座標 e.getPoint() は、ImagePanel内の座標)
	 */
	public void processPanelClick(MouseEvent e) {
		pos.x=e.getPoint().x;
		pos.y=e.getPoint().y;
		select(imagePanel.clicked(e.getPoint()));
	}
	/**
	 * commentArea 上で、内容修正が行われたときの処理。
	 */
	public void setComment() {
		// サンプルってことで、実装済み
		if(manager.getSelected()!=null) {
			manager.getSelected().setComment(commentArea.getText());
		}
	}

	/**
	 * 鎌田が趣味で作成した JTextComponent 用右クリポップアップルーチン
	 * @param e
	 */
	public void textPopup(MouseEvent e) {
		if(!e.isPopupTrigger()) return;
		JTextComponent target = (JTextComponent)e.getSource();
		JPopupMenu pop = new JPopupMenu();
		pop.add(new DefaultEditorKit.CopyAction());
		if(target.isEditable()) {
			pop.add(new DefaultEditorKit.CutAction());
			pop.add(new DefaultEditorKit.PasteAction());
		}
		pop.show(target, e.getX(),  e.getY());
	}

	/**
	 * Create the frame.
	 * 以下は、基本GUIツールが生成したものです。
	 * 鎌田が手で書いたのは、一部の局所変数をフィールドに格上げ+上記ポップアップ用ハンドラの追加ぐらいです。
	 * よっぽどの上級者以外は、***** 基本的に読む必要ありません*****。
	 * 読んでためになるといても、EventListener の追加（anonymous inner classの利用例）ぐらいかな？
	 */
	public ImageViewer() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				manager.saveContext();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		imagePanel = new ImagePanel();
		
		imagePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getClickCount()>=2){
					doubleClick(e);
				}
				else{
				int btn = e.getButton();
				if (btn == MouseEvent.BUTTON1)
				processPanelClick(e);
				}
				repaint();
				pos.x=e.getX();
				pos.y=e.getY();
			}
		});
		imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				drag(e);
				repaint();
			}
		});
		contentPane.add(imagePanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(panel, BorderLayout.EAST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		panel_1.setMaximumSize(new Dimension(32767, 0));
		panel_1.setBorder(null);
		panel.add(panel_1);

		conditionField = new JTextField();
		conditionField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				textPopup(e);
			}
		});
		
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		panel_1.add(conditionField);
		conditionField.setColumns(10);

		sortButton = new JButton("sort");
		sortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sort();
					repaint();
				} catch (ParseException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}
				
			}

		});
		panel_1.add(sortButton);

		JPanel panel_2 = new JPanel();
		panel_2.setMaximumSize(new Dimension(32767, 0));
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "properties", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{22, 111, 0};
		gbl_panel_2.rowHeights = new int[]{19, 19, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);

		JLabel lblSize = new JLabel("size");
		GridBagConstraints gbc_lblSize = new GridBagConstraints();
		gbc_lblSize.anchor = GridBagConstraints.EAST;
		gbc_lblSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblSize.gridx = 0;
		gbc_lblSize.gridy = 0;
		panel_2.add(lblSize, gbc_lblSize);

		sizeField = new JTextField();
		sizeField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				textPopup(e);
			}
		});
		sizeField.setEditable(false);
		GridBagConstraints gbc_sizeField = new GridBagConstraints();
		gbc_sizeField.fill = GridBagConstraints.HORIZONTAL;
		gbc_sizeField.insets = new Insets(0, 0, 5, 0);
		gbc_sizeField.gridx = 1;
		gbc_sizeField.gridy = 0;
		panel_2.add(sizeField, gbc_sizeField);
		sizeField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("date");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		panel_2.add(lblNewLabel_2, gbc_lblNewLabel_2);

		dateField = new JTextField();
		dateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				textPopup(e);
			}
		});
		dateField.setEditable(false);
		GridBagConstraints gbc_dateField = new GridBagConstraints();
		gbc_dateField.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateField.gridx = 1;
		gbc_dateField.gridy = 1;
		panel_2.add(dateField, gbc_dateField);
		dateField.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		commentArea = new JTextArea();
		commentArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				setComment();
			}
		});
		commentArea.setRows(1);
		scrollPane.setViewportView(commentArea);
		commentArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				textPopup(e);
			}
		});
		commentArea.setColumns(20);
		repaint();
	}

}
