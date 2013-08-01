/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wbitoolkit.phylo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import wbitoolkit.phylo.TreeAnnotation.AnnotationType;

/**
 *
 * @author wb
 */
public class TreeGUI extends JPanel{
    
    public static final int RECTANGULAR=0;
    
    
    private Tree2D ts;
    private int style;
    private Tree tree;
    private List<TreeAnnotation> annotations;
    private BufferedImage img;
    private boolean showLabel=true;
    private boolean showBranchLength=false;
    private boolean showProb=true;
    private double zoom=1;
    private int height=0;
    private JScrollPane view;
    private JPanel wrap;
    private JPanel topToolbar;
    private JPopupMenu popupMenu;
    private JMenuItem searchItem;
    private JTextField searchField;
    private ArrayList<TreeAnnotation> lastSearch;
    
    public TreeGUI(Tree tree){
        this.tree=tree;  
        annotations=new LinkedList();
        view=new JScrollPane(this);
        wrap=new JPanel();
        wrap.setLayout(new BorderLayout());
        wrap.add(view);
        topToolbar=makeTopToolbar();
        popupMenu=new JPopupMenu();
        searchItem=new JMenuItem("Search");
        searchItem.addActionListener(showTopToolbarListener());
        popupMenu.add(searchItem);
        this.setComponentPopupMenu(popupMenu);
        lastSearch=new ArrayList();
        
        this.addComponentListener(heightListner());
        this.addMouseListener(centerEvent());
        this.addMouseWheelListener(zoomEvent());
    }
    
    public JPanel getPanel(){
        return wrap;
    }

    private JPanel makeTopToolbar(){
        JPanel top=new JPanel();
        top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
        searchField=new JTextField();
        searchField.addActionListener(searchListener());
        top.add(searchField);
        JButton searchButton=new JButton("Search");
        searchButton.addActionListener(searchListener());
        top.add(searchButton);
        JButton closeButton=new JButton("X");
        closeButton.addActionListener(closeTopToolbarListener());
        top.add(closeButton);
        return top;
    }
    
    private ActionListener searchListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String field=searchField.getText();
                Color c=new Color(255,163,24);
                if(lastSearch.size()>0){
                    for(TreeAnnotation a:lastSearch){
                        removeAnnotation(a);
                    }
                    lastSearch.clear();
                }
                String[] labels=tree.getLabels();
                Pattern p=Pattern.compile(field,Pattern.CASE_INSENSITIVE);
                Matcher m;
                for(String label:labels){
                    m=p.matcher(label);
                    if(m.find()){
                        TreeNode hit=tree.getLeafByName(label);
                        TreeAnnotation a1=new RectangularTreeAnnotation(getTree2D(), hit, c, AnnotationType.BRANCH);
                        TreeAnnotation a2=new RectangularTreeAnnotation(getTree2D(), hit, c, AnnotationType.LABEL);
                        lastSearch.add(a1);
                        lastSearch.add(a2);
                        addAnnotation(a1);
                        addAnnotation(a2);
                    }
                }             
                repaint();
            }
        };       
    }
    
    private ActionListener closeTopToolbarListener(){
        return new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                wrap.remove(topToolbar);
                wrap.validate();
            }
            
        };
    }
    
    private ActionListener showTopToolbarListener(){
        return new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                wrap.add(topToolbar,BorderLayout.NORTH);
                wrap.validate();
                searchField.requestFocusInWindow();
            }
            
        };
    }
    
    private ComponentAdapter heightListner(){
        return new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if(height==0){ 
                    height=getHeight();
                }
            }
        };
    }
    private MouseAdapter centerEvent(){
        return new MouseAdapter(){
           @Override
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount()==2){
                    zoom=1;
                    setPreferredSize(new Dimension((int)(getWidth()),height));
                    view.setViewportView(TreeGUI.this);
                    repaint();
                }
            } 
            @Override
            public void mouseMoved(MouseEvent me) {
//                System.out.println(me.getY()+","+me.getY()*zoom);
            }
        };
    }
    
    private MouseWheelListener zoomEvent(){
        return new MouseWheelListener(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe){
                double y=mwe.getY()/zoom;
                double z=mwe.getWheelRotation()*0.1;
                if(height*(zoom+z)<=10) return;
                zoom+=z;
                setPreferredSize(new Dimension((int)(getWidth()),(int)(height*zoom)));
                view.setViewportView(TreeGUI.this);
                JScrollBar v=view.getVerticalScrollBar();
                double d=mwe.getY()-v.getValue();
                v.setValue((int)(y*zoom-d));
                repaint();
            }
        };
    }
    

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.white);
        int width = getWidth();
        int height=(int)(this.height*zoom);
        
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = (Graphics2D) img.getGraphics();
        gg.setColor(Color.WHITE);
        gg.fillRect(0, 0, width, height);

        draw(gg, width, height, style);

        g2d.drawImage(img, 0, 0, this);
    }
    
    public void draw(Graphics2D g2, int width, int height, int style){       
        switch(style){
            case 0: {
                ts=new RectangularTree2D(tree, width, height, g2);
                break;
            }
            default:
                return;
        }
        ts.setShowBranchLength(showBranchLength);
        ts.setShowLabel(showLabel);
        ts.setShowProb(showProb);
        ts.draw();
        
        if(getAnnotations().size()>0){
            for(TreeAnnotation a:annotations){
                a.setTree(ts);
                a.drawAnnotation();
            }
        }
    }
    
    
    public void addAnnotation(TreeAnnotation na){
        getAnnotations().add(na);
    }
    
    public void removeAnnotation(TreeAnnotation na){
        getAnnotations().remove(na);
    }

    public void clearAnnotation(){
        getAnnotations().clear();
    }
    /**
     * @return the tree2d
     */
    public Tree2D getTree2D() {
        return ts;
    }

    /**
     * @param ts the tree2d to set
     */
    public void setTree2D(Tree2D ts) {
        this.ts = ts;
    }

    /**
     * @return the style
     */
    public int getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(int style) {
        this.style = style;
    }

    /**
     * @return the tree
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * @param tree the tree to set
     */
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    /**
     * @return the showLabel
     */
    public boolean isShowLabel() {
        return showLabel;
    }

    /**
     * @param showLabel the showLabel to set
     */
    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    /**
     * @return the showBranchLength
     */
    public boolean isShowBranchLength() {
        return showBranchLength;
    }

    /**
     * @param showBranchLength the showBranchLength to set
     */
    public void setShowBranchLength(boolean showBranchLength) {
        this.showBranchLength = showBranchLength;
    }

    /**
     * @return the showProb
     */
    public boolean isShowProb() {
        return showProb;
    }

    /**
     * @param showProb the showProb to set
     */
    public void setShowProb(boolean showProb) {
        this.showProb = showProb;
    }

    /**
     * @return the annotations
     */
    public List<TreeAnnotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(List<TreeAnnotation> annotations) {
        this.annotations = annotations;
    }


}
