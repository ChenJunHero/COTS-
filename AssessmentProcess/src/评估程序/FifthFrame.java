package ��������;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore.PrivateKeyEntry;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FifthFrame extends JFrame {
	protected static final boolean True = false;
	JButton bt1 = new JButton("����");
	private List<DriverInfo> driverInfos = new ArrayList<>();
	private int riskBorder[][][];
	private float riskProp[][];
	
	private int num[];
	private float ie[];
	
	private float result[][];
	private float resultnocos[][];
	private float risknocosAll;
	private float risknocos;
	private float cosBate;
	private float risk;
	private float riskmax;
	private float riskmaxAll;
	private float normlizationrisk;
	
	private int count = 0;
	
	public static float EXTREME = 0.9f;
	public static float SEVERAL = 0.6f;
	public static float SIGNIFICANT = 0.3f;
	public static float GENERAL = 0.1f;
	
	public FifthFrame(){
		//1. ���ȶ�ȡriskLevel COTS���� ������DriverInfo�����ʾ��Щ��Ϣ
		try {
			File file = Tools.currentDriverInfoFile;
//			File file = new File("ϵͳB����");

			LineNumberReader rf = new LineNumberReader(new FileReader(file)); 
	
			if (rf != null) { 
				rf.skip(file.length()); 
				count = rf.getLineNumber(); 
				rf.close(); 
			} 
			System.out.println("count = " + count);
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file)));
			String buffer;
		
			while((buffer = br.readLine()) != null){
				String[] str = buffer.split(" ");
                System.out.println(buffer);
				DriverInfo driverInfo = new DriverInfo();
				driverInfo.setDriver(str[0]);
				driverInfo.setRiskIndex(Integer.valueOf(str[1]));
				driverInfo.setCosValue(Float.valueOf(str[2]));
				driverInfos.add(driverInfo);
				
			}
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//2. ��ȡӰ������֮��ı߽�ֵ����
		try {
//			System.out.println("count = " + count);
			riskBorder = new int[count][count][2];
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(Tools.currentDriverrulesFile)));
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("drivers����"))));

			String buffer;
			while ((buffer = br.readLine()) != null) {
//				list.add(buffer);
				String[] str = buffer.split(" ");
//				System.out.println(buffer);
				riskBorder[Integer.valueOf(str[0])]
						[Integer.valueOf(str[1])]
								[Integer.valueOf(str[2])] 
										= Integer.valueOf(str[3]);
			}
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//3. ��ʼ����riskProb
		riskProp = new float[count][count];
		for (int i=0; i<count; i++){
			for (int j = i+1; j < count; j++) {
				if (riskBorder[i][j][0] == 0 || riskBorder[i][j][1] == 0){
					riskProp[i][j] = 0;
					
				} else if (riskBorder[i][j][0] <=  driverInfos.get(i).getRiskIndex()+1 
						&& riskBorder[i][j][1] <=  driverInfos.get(j).getRiskIndex()+1){
					
					int temp = driverInfos.get(i).getRiskIndex()  + driverInfos.get(j).getRiskIndex();
					switch (temp) {
					case 5:
						riskProp[i][j] = GENERAL;
						break;
					case 6:
						riskProp[i][j] = SIGNIFICANT;
						break;
					case 7:
						riskProp[i][j] = SEVERAL;
						break;
					case 8:
						riskProp[i][j] = EXTREME;
						break;
					default:
						break;
				
					}
				} else {
					riskProp[i][j] = 0;
				}
			}
		}
		for (int i=0; i<count; i++){
			for (int j=0; j<count; j++){
//				if (riskProp[i][j] != 0){
					
					System.out.println("riskProp[i][j] = " + riskProp[i][j]);
//				}
			}
		}
		
		
		//4. CE���ü��� ֱ��ʹ��driverInfos������ݼ���
		
		//5. IE����
		num = new int[count];
		ie = new float[count];
		for (int i=0; i<count; i++){
			for (int j=0; j<count; j++){
//				if (riskProp[i][j] != 0){
				if (riskBorder[i][j][0] > 0 || riskBorder[i][j][1] >0){
					num[i]++;
					num[j]++;
				}
			}
		}
		for (int i=0; i<count; i++){
			ie[i] = (float) (1 + 0.1 * num[i]);
			System.out.println("ie[i] = " + ie[i]);
		}
		
		
		//6. �ܼ���
		result = new float[count][count];
		for (int i=0; i<count; i++){
			for (int j=i+1; j<count; j++){
				result[i][j] = riskProp[i][j] * ie[i] * ie[j] * driverInfos.get(i).getCosValue() * driverInfos.get(j).getCosValue();//ԭʼֵ����
				System.out.println("result[i][j] = " + result[i][j]);
				risk += result[i][j];
				if(riskBorder[i][j][0] != 0 && riskBorder[i][j][1] !=0){
					riskmax = (float) (0.9 * ie[i] * ie[j]*driverInfos.get(i).getCosValue() * driverInfos.get(j).getCosValue());
					riskmaxAll += riskmax;
					System.out.println("�������ֵ = " + riskmax);
					System.out.println("�������ֵ ֮= " + riskmaxAll);
					
					//������ce��ֵ
					risknocos = (float) (riskProp[i][j] * ie[i] * ie[j]);
					risknocosAll += risknocos;
					System.out.println("risknocos =" + risknocos);
				}
				
			}
		}
		
		System.out.println("risk = " + risk);
		System.out.println("riskmaxAll = " + riskmaxAll);
		System.out.println("risknocosAll = " + risknocosAll);
		//��׼��risk��0~100֮��
			
			normlizationrisk = (risk/riskmaxAll) *100;//��׼����ʽ
			
		System.out.println("normlizationrisk = " + normlizationrisk);
		//cots���ձ�
		    cosBate = (risk/risknocosAll);
		    System.out.println("cosBate = " + cosBate);
        JPanel p = new JPanel();
        p.setLayout(BorderLayout());
		JPanel p1 = new JPanel();	

		p1.setLayout(new  GridLayout(0,1));
	

		JLabel j1 = new JLabel("ԭʼֵ�� System risk=" + risk,JLabel.CENTER);
		JLabel j2 = new JLabel("���ֵ�� Max of System risk=" + riskmaxAll,JLabel.CENTER);
		JLabel j3 = new JLabel("��Сֵ�� Min of System risk= 0.0" ,JLabel.CENTER);
		JLabel j4 = new JLabel("��׼ֵ����׼����0~100���� Std of System risk=" + normlizationrisk,JLabel.CENTER);
		JLabel j5 = new JLabel("��COTS����ϵͳ��ȫֵ: System risk=" + risknocosAll,JLabel.CENTER);
		JLabel j6 = new JLabel("COTS���ձȣ� COTS Rate of System risk=" + cosBate*100 + "%",JLabel.CENTER);
//	
	    p1.add(j1);
	    p1.add(j2);
	    p1.add(j3);
	    p1.add(j4);
	    p1.add(j5);
	    p1.add(j6);
//	    p1.add(jbt1);
	  //������������
	    JPanel p2 = new JPanel();
//	    p2.setLayout(BorderLayout());
	    p2.add(bt1);
	    add(p2,BorderLayout.SOUTH);
		add(p1,BorderLayout.CENTER);
		//�����¼�
		bt1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("���������");
				chooser.setMultiSelectionEnabled(True);
				chooser.showSaveDialog(null);
				chooser.setDialogType(JFileChooser.FILES_AND_DIRECTORIES );
				
				File file = chooser.getSelectedFile();
        	   try {
				BufferedWriter bw = new BufferedWriter(
						   new OutputStreamWriter(new FileOutputStream(file)));
				
	        		   bw.append("ԭʼֵ�� System risk=" + risk +"\n");
	        		   bw.append("���ֵ�� Max of System risk=" + riskmaxAll+ "\n");
	        		   bw.append("��Сֵ�� Min of System risk= 0.0" + "\n");
	        		   bw.append("��׼ֵ����׼����0~100���� Std of System risk=" + normlizationrisk + "\n");
	        		   bw.append("��COTS����ϵͳ��ȫֵ: System risk=" + risknocosAll + "\n");
	        		   bw.append("COTS���ձȣ� COTS Rate of System risk=" + cosBate*100 + "%" + "\n");
				bw.flush();
				bw.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	   
          
    			
			}});
				
				// TODO Auto-generated method stub
				
		
		
		setTitle("��Ϣ��ȫ����ֵ����");
		setSize(550, 635);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	private LayoutManager BorderLayout() {
		// TODO Auto-generated method stub
		return null;
	}
	private LayoutManager FlowLayout(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String args[]){
		new FifthFrame();
	}

}
