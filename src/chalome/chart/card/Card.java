/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chalome.chart.card;

import chalome.shadow.ShadowRenderer;
import com.raven.card.ModelCard;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 *
 * @author Dell
 */
public class Card extends javax.swing.JPanel {

    public Color getGradientColor() {
        return gradientColor;
    }

    public void setGradientColor(Color gradientColor) {
        this.gradientColor = gradientColor;
    }

    private Color gradientColor;
    public Card() {
        initComponents();
             setOpaque(false);
        gradientColor = new Color(7, 53, 111);
        setBackground(new Color(0, 79, 181));
    }
 public void setData(ModelCard data) {
        lbTitle.setText(data.getTitle());
        lbValues.setText(data.getValues());
        lbDescription.setText(data.getDescription());
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int shadowSize = 10;
        int width = getWidth() - shadowSize * 2;
        int height = getHeight() - shadowSize * 2;
        createShadow(g2, create(0, 0, width, height), shadowSize);
        GradientPaint gra = new GradientPaint(0, 0, getBackground(), 0, height, gradientColor);
        g2.setPaint(gra);
        g2.fill(create(shadowSize, shadowSize, width, height));
        g2.dispose();
        super.paintComponent(grphcs);
    }

    private void createShadow(Graphics2D g2, Shape shape, int shadowSize) {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(shape);
        g2.drawImage(new ShadowRenderer(shadowSize, 0.35f, new Color(30, 30, 30)).createShadow(img), 0, 0, null);
    }

    private Shape create(int x, int y, int width, int height) {
        int borrder = 30;
        Area area = new Area(new RoundRectangle2D.Double(x, y, width, height, borrder, borrder));
        area.add(new Area(new Rectangle2D.Double(x, height - borrder + y, borrder, borrder)));
        area.add(new Area(new Rectangle2D.Double(x + width - borrder, y, borrder, borrder)));
        return area;
    }

    public void setIcon(Icon icon) {
        lbIcon.setIcon(icon);
    }

    public Icon getIcon() {
        return lbIcon.getIcon();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        lbIcon = new javax.swing.JLabel();
        lbDescription = new javax.swing.JLabel();
        lbValues = new javax.swing.JLabel();

        lbTitle.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(190, 190, 190));
        lbTitle.setText("Title");

        lbIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/profit.png"))); // NOI18N

        lbDescription.setForeground(new java.awt.Color(190, 190, 190));
        lbDescription.setText("Description");

        lbValues.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbValues.setForeground(new java.awt.Color(190, 190, 190));
        lbValues.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbValues, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbValues, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbIcon;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbValues;
    // End of variables declaration//GEN-END:variables
}
