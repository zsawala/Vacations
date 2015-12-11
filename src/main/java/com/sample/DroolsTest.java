package com.sample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import javax.swing.BorderFactory;



/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	

	public static StatefulKnowledgeSession ksession;
	public static KnowledgeRuntimeLogger logger;
	public static KnowledgeBase kbase;
	public static Pytanie pytanie ;
    public static final void main(String[] args) throws IOException {
    	
    	
    	//REGU�Y
        try {
            // load up the knowledge base
            kbase = readKnowledgeBase();
            ksession = kbase.newStatefulKnowledgeSession();
            logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
        } catch (Throwable t) {
            t.printStackTrace();
        }
       	 	
    	
    	//INTERFACE
    	
    	final JFrame frame = new JFrame("Vacations"); //okno
    	final JPanel panel = new JPanel(); //panel powitalny
    	//final JPanel panel_pytania = new JPanel(); //panel z pytaniami
    	
    	//napis
    	
    	JLabel napis = new JLabel("<html><center> VACATION</center><center> FINDER <center></html>");
    	Font myFont = new Font("Tahoma", Font.BOLD, 40);
    	Font buttonFont = new Font("Tahoma", Font.BOLD, 25);
    	napis.setFont(myFont);
    	napis.setHorizontalAlignment(SwingConstants.CENTER);
    	napis.setBounds(175,325,250,100);
    	napis.setForeground(Color.decode("#555555"));
    	napis.setVisible(true);
    	
    	//obrazek logo
    	BufferedImage logoPicture = ImageIO.read(new File("logo.png"));
    	JLabel logoLabel = new JLabel(new ImageIcon(logoPicture));
    	logoLabel.setBounds(0,0,logoPicture.getWidth(),logoPicture.getHeight());
    	logoLabel.setVisible(true);
    	
    	// ustawienia okna
    	frame.setSize(600,700);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setVisible(true);
    	
    	//ustawienia panelu powitalnego
    	panel.setBackground(Color.decode("#84C3BB"));
    	panel.setLayout(null);
    	frame.add(panel);
    	
    	//ustawienia panelu startowego
    	JButton start = new JButton("START");
    	start.setBounds(210,500,180,70);
    	start.setBorder(BorderFactory.createLineBorder(Color.decode("#555555"),4));
    	start.setFont(buttonFont);
    	start.setForeground(Color.decode("#555555"));
    	start.setContentAreaFilled(false);
    	start.setVisible(true);
    	panel.add(start);
    	panel.add(logoLabel);
    	panel.add(napis);
    	
    	//ustawienia poczatkowe panelu z pytaniami
    	start.addActionListener(new ActionListener()
    	{
    		  public void actionPerformed(ActionEvent e)
    		  {
    			  panel.removeAll();
    			  panel.revalidate();
    			  panel.repaint();
    			//PYTANIE POCZ�TKOWE
  		    	pytanie = new Pytanie("Wakacje gdzie bardzie?",panel);
  		    	ksession.insert(pytanie);
  		    	pytanie.ask("Wakacje gdzie bardziej ci� interesuj�?",new String[]{"Polska","Europa","�wiat"});
    		  }
    	});
    	
    	//ZAMKNIECIE LOGERA NA WY��CZENIE PROGRAMU
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    	    public void run() {
    	    	logger.close();
    	    }
    	}));

    	
    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("Sample.drl"), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }


}
