package ��������;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.synth.SynthOptionPaneUI;

public class SecondFrame extends JFrame {
	protected static final boolean False = false;

	private JButton bt1, bt2, bt3,bt4;

	// private String[] Drivers = { "MS", "CV", "IA", "SDC", "SIZE", "CPM",
	// "AUT", "SA", "CE", "KS", "FR", "PM", "MP",
	// "RP", "AC", "VDD", "others"};
	// //JComboBox jcb=new JComboBox(Drivers);

	private JTextField textField = new JTextField(10);
//	private JTextArea textArea = new JTextArea(27, 33);
	JList jls = new JList();
	private int flag;

	List<String> list = new ArrayList<>();

	public SecondFrame() {
		bt1 = new JButton("����");
		bt2 = new JButton("����");
		bt3 = new JButton("���");
		bt4 = new JButton("ɾ��");
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout());
		p1.add(bt1);
		p1.add(bt2);
		add(p1, BorderLayout.SOUTH);
		// bt1.setBounds(100,100,100,100);
		// add(jlst, BorderLayout.NORTH);
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout());
		p2.add(textField);
		p2.add(bt3);
		p2.add(bt4);
		// p2.setLayout(new BorderLayout());
		// p2.add(bt3,BorderLayout.EAST);
		// p2.add(jcb,BorderLayout.WEST);
		add(p2, BorderLayout.NORTH);
		// add(new JScrollPane(jcb),BorderLayout.NORTH);
		/*JPanel p3 = new JPanel();

		p3.add(jls);*/

		add(jls, BorderLayout.CENTER);

		bt3.addActionListener(new ActionListener() {
			// ����¼�
			@Override
			public void actionPerformed(ActionEvent e) {
				// ���Ȼ���û�����// TODO Auto-generated method stub
				String str = textField.getText();

				if (str == null || str.equals("")) {
					System.out.println("����Ϊ�գ��޷����");
					return;
				}
				// �����������list�б���

				System.out.println("str = " + str);
				list.add(str);

				// �ַ�������
				StringBuilder sb = new StringBuilder();

				// ��list������������ı�����
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i) + "\n");

				}
//				Collections.as
//				JList jls = new JList(list.toArray());
				jls.setListData(list.toArray());
//				jls.setText(sb.toString());
				// ���ԭ������
				textField.setText(null);
			}

		});
		
		//ɾ����ť����¼�
		bt4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int indexs[] = jls.getSelectedIndices();//getSelectedIndex();
				
				for (int index: indexs){
					System.out.println("index = " + index);
//					list.remove(index);
				}
				list.removeAll(Arrays.asList(jls.getSelectedValues()));
				jls.setListData(list.toArray());
			}
		});
		// ����ı����¼����ļ���
		bt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
				// java.io.File file2= new java.io.File("filename.txt");
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("������۶����ļ�");
				chooser.setMultiSelectionEnabled(False);
				chooser.showSaveDialog(null);
				chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES );
				File file = chooser.getSelectedFile();
				Tools.currentDriverFile = file;
				
				if (flag == JFileChooser.APPROVE_OPTION) {
//					// ��ø��ļ�
//
					System.out.println("open file" + file.getName());
				}
//
				String filename;
				String filepath;
				java.io.File file2 = null;
//				// �������ȷ����ť�����ø��ļ���
				if (flag == JFileChooser.APPROVE_OPTION) {

					filename = chooser.getName(file);
					System.out.println("filename = " + filename);

				}

				if (chooser.getSelectedFile() == null) {
					System.out.println("�뱣���ļ�");
				} else {
//					 Tools.currentDriverFileName = file.getAbsolutePath();

				}

				try {
					java.io.PrintWriter output = new java.io.PrintWriter(file);
					StringBuilder sb = new StringBuilder();
					// �����������ı�����
					for (int i = 0; i < list.size(); i++) {
						sb.append(list.get(i) + "\n");
					}
					System.out.println(sb.toString());
					output.print(sb.toString());
					output.flush();
					output.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}

		});
		// ������ʾ
		bt2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("ѡ����۶���");//
				chooser.setMultiSelectionEnabled(true);
//				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt","txt");
//				chooser.setFileFilter(filter);//
				chooser.showOpenDialog(null);
				chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES );

				File file = chooser.getSelectedFile();
				System.out.println("file = " + file.getAbsolutePath());

				Tools.currentDriverFile = file;
//				Tools.currentDriverFileName = file.getAbsolutePath();
				// �����ص��ļ���ʾ���ı�����
				list = new ArrayList<>();
				jls.setListData(list.toArray());
				
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));// ��������Ҫ��txt�ļ�
					String buffer;//
//					StringBuilder sb = new StringBuilder();
					while ((buffer = br.readLine()) != null) {
//						sb.append(buffer + "\n");// TODO Auto-generated method
						list.add(buffer);
					}
					
					jls.setListData(list.toArray());
					
//					System.out.println(sb.toString());
//					jls.setToolTipText(sb.toString());
				} catch (Exception e1) {

					e1.printStackTrace();
				}
			}
		});
		setTitle("���۶�������");
		setSize(550, 630);
		setLocationRelativeTo(null);
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

}
