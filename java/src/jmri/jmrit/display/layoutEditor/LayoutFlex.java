package jmri.jmrit.display.layoutEditor;

import static jmri.jmrit.display.layoutEditor.LayoutTrack.NONE;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import jmri.BlockManager;
import jmri.InstanceManager;
import jmri.jmrit.display.layoutEditor.blockRoutingTable.LayoutBlockRouteTableAction;
import jmri.util.JmriJFrame;
import jmri.util.swing.JmriBeanComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A LayoutFlex is a bendable track segment on a layout that can be shaped to one or more curves.
 * <P>
 * A LayoutFlex has four or more control points. The first and last are connection
 * points to adjacent track segments.
 * <P>
 * LayoutFlex's carry Block information, as do TrackSegments, LayoutTurnouts and LevelXings.
 * <P>
 * For drawing purposes, each LayoutFlex carries a center point and displacements
 * for the control points. The center point and the control point displacements
 * may be adjusted by the user when in edit mode.
 * <P>
 * When LayoutFlexs are first created, there are no connections. Block
 * information and connections are added when available.
 * <P>
 * LayoutFlex's may be drawn as dashed lines or solid lines. In addition
 * LayoutFlex's may be hidden when the panel is not in EditMode.
 *
 * @author George Warner Copyright (c) 2017
 */
public class LayoutFlex extends LayoutTrack {

    // Defined text resource
    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrit.display.layoutEditor.LayoutEditorBundle");

    // defined constants
    // operational instance variables (not saved between sessions)
    private LayoutBlock block = null;
    private LayoutFlex instance = null;
    private LayoutEditor layoutEditor = null;

    // persistent instances variables (saved between sessions)
    private String blockName = "";

    private Object connectA = null;
    private Object connectB = null;

    private Point2D center = new Point2D.Double(50.0, 50.0);
    private ArrayList<Point2D> controlPoints = new ArrayList<Point2D>(); // list of control point displacements
    /**
     * constructor method
     */
    public LayoutFlex(String id, Point2D c, LayoutEditor myPanel) {
        instance = this;
        layoutEditor = myPanel;
        ident = id;
        center = c;

        // set default controlPoints displacements
        controlPoints.add(new Point2D.Double(-20.0, 0.0));
        controlPoints.add(new Point2D.Double(-10.0, -10.0));
        controlPoints.add(new Point2D.Double(+10.0, +10.0));
        controlPoints.add(new Point2D.Double(+20.0, 0.0));
    }

    // this should only be used for debugging…
    public String toString() {
        return "LayoutFlex " + ident;
    }

    /**
     * Accessor methods
     */
    public String getID() {
        return ident;
    }

    public String getBlockName() {
        return blockName;
    }

    public Object getConnection(int location) throws jmri.JmriException {
        switch (location) {
            case FLEX_A:
                return connectA;
            case FLEX_B:
                return connectB;
            default:
                log.warn("Unhandled loc: {}", location);
                break;
        }
        log.error("Invalid Point Type " + location); //I18IN
        throw new jmri.JmriException("Invalid Point");
    }

    public void setConnection(int location, Object o, int type) throws jmri.JmriException {
        if ((type != TRACK) && (type != NONE)) {
            log.error("unexpected type of connection to layoutturnout - " + type);
            throw new jmri.JmriException("unexpected type of connection to layoutturnout - " + type);
        }
        switch (location) {
            case FLEX_A:
                connectA = o;
                break;
            case FLEX_B:
                connectB = o;
                break;
            default:
                log.error("Invalid Point Type " + location); //I18IN
                throw new jmri.JmriException("Invalid Point");
        }
    }

    public Object getConnectA() {
        return connectA;
    }

    public Object getConnectB() {
        return connectB;
    }

    public void setConnectA(Object o, int type) {
        connectA = o;
        if ((connectA != null) && (type != TRACK)) {
            log.error("unexpected type of A connection to LayoutFlex - " + type);
        }
    }

    public void setConnectB(Object o, int type) {
        connectB = o;
        if ((connectB != null) && (type != TRACK)) {
            log.error("unexpected type of B connection to LayoutFlex - " + type);
        }
    }

    public LayoutBlock getLayoutBlock() {
        if ((block == null) && (blockName.length() > 0)) {
            block = layoutEditor.provideLayoutBlock(blockName);
        }
        return block;
    }

    public Point2D getCoordsCenter() {
        return center;
    }

    public Point2D getCoordsA() {
        return getCoordsN(0);
    }

    public Point2D getCoordsB() {
        return getCoordsN(-1);
    }

    public Point2D getCoordsN(int index) {
        Point2D result = center;
        if (index < 0) {
            index += controlPoints.size();
        }
        if ((index >= 0) && (index < controlPoints.size())) {
            result = controlPoints.get(index);
            double px = center.getX() + result.getX();
            double py = center.getY() + result.getY();
            result = new Point2D.Double(px, py);
        }
        return result;
    }

    public Point2D getCoordsForConnectionType(int connectionType) {
        Point2D result;
        switch (connectionType) {
            case FLEX_A:
                result = getCoordsA();
                break;
            case FLEX_B:
                result = getCoordsB();
                break;
            case SLIP_CENTER:
                result = center;
                break;
            case FLEX_CONTROL_POINT_OFFSET:
                //TODO: add code for control points here!
                result = center;
                break;
            default:
                if ((connectionType >= FLEX_CONTROL_POINT_OFFSET) && 
                        (connectionType < TURNTABLE_RAY_OFFSET)) {
                    result = getCoordsN(connectionType - FLEX_CONTROL_POINT_OFFSET);
                } else {
                    log.error("Invalid connection type " + connectionType); //I18IN
                    throw new jmri.JmriException("Invalid connection ");
                }
        }
        return result;
    }

    /**
     * Add Layout Block
     */
    public void setLayoutBlock(LayoutBlock b) {
        block = b;
        if (b != null) {
            blockName = b.getID();
        }
    }

    private void updateBlockInfo() {
        LayoutBlock b1 = null;
        if (block != null) {
            block.updatePaths();
        }
        if (connectA != null) {
            b1 = ((TrackSegment) connectA).getLayoutBlock();
            if ((b1 != null) && (b1 != block)) {
                b1.updatePaths();
            }
        }
        if (connectB != null) {
            b1 = ((TrackSegment) connectB).getLayoutBlock();
            if ((b1 != null) && (b1 != block)) {
                b1.updatePaths();
            }
        }
        reCheckBlockBoundary();
    }

    public void reCheckBlockBoundary() {
        if (connectA == null && connectB == null) {
        } else if (connectA == null || connectB == null) {
            //could still be in the process of rebuilding the point details
            return;
        }

        TrackSegment trkA;
        TrackSegment trkB;

        if (connectA instanceof TrackSegment) {
            trkA = (TrackSegment) connectA;
            if (trkA.getLayoutBlock() == block) {
            }
        }
        if (connectB instanceof TrackSegment) {
            trkB = (TrackSegment) connectB;
            if (trkB.getLayoutBlock() == block) {
            }
        }
    }

    /**
     * Methods to test if mainline track or not Returns true if either
     * connecting track segment is mainline Defaults to not mainline if
     * connecting track segments are missing
     */
    public boolean isMainline() {
        return (((connectA != null) && (((TrackSegment) connectA).getMainline())) ||
            ((connectB != null) && (((TrackSegment) connectB).getMainline())));
    }

    /**
     * return the connection type for a point
     *
     * @since 7.4.?
     */
    public int connectionTypeForPoint(Point2D p, boolean useRectangles, boolean requireUnconnected) {
        int result = NONE;  // assume point not on connection

        if (useRectangles) {
            Rectangle2D r = layoutEditor.turnoutCircleRectAt(p);

            if (!requireUnconnected) {
                //check the center point
                if (r.contains(center)) {
                    result = LayoutTrack.FLEX_CENTER;
                }
            }

            if (!requireUnconnected || (getConnectA() == null)) {
                //check the A connection point
                if (r.contains(getCoordsA())) {
                    result = LayoutTrack.FLEX_A;
                }
            }

            if (!requireUnconnected || (getConnectB() == null)) {
                //check the B connection point
                if (r.contains(getCoordsB())) {
                    //mouse was pressed on this connection point
                    result = LayoutTrack.FLEX_B;
                }
            }
        } else {
            double circleRadius = 3 * layoutEditor.getTurnoutCircleSize();

            if (!requireUnconnected) {
                //check the center point
                if (p.distance(center) <= circleRadius) {
                    result = LayoutTrack.FLEX_CENTER;
                }
            }

            if (!requireUnconnected || (getConnectA() == null)) {
                //check the A connection point
                if (p.distance(getCoordsA()) <= circleRadius) {
                    result = LayoutTrack.FLEX_A;
                }
            }

            if (!requireUnconnected || (getConnectB() == null)) {
                //check the B connection point
                if (p.distance(getCoordsB()) <= circleRadius) {
                    //mouse was pressed on this connection point
                    result = LayoutTrack.FLEX_B;
                }
            }
        }
        //TODO: add hit testing for the control points

        return result;
    }

    /**
     * Modify coordinates methods
     */
    public void setCoordsCenter(Point2D p) {
        center = p;
    }

    public Point2D setCoordsA(Point2D p) {
        return setCoordsN(0, p);
    }

    public Point2D setCoordsB(Point2D p) {
        return setCoordsN(-1, p);
    }

    public Point2D setCoordsN(int index, Point2D p) {
        Point2D result = new Point2D.Double(p.getX() - center.getX(), p.getY() - center.getY());
        if (index < 0) {
            index += controlPoints.size();
        }
        if ((index >= 0) && (index < controlPoints.size())) {
            controlPoints.set(index, result);
        }
        return result;
    }

    protected Point2D addControlPoint(Point2D p) {
        p.setLocation(p.getX() - center.getX(), p.getY() - center.getY());
        controlPoints.add(p);
        return p;
    }

    private int getNewIndex() {
        return controlPoints.size();
    }

    // the following method is only for use in loading layout turntables
    // note: p should already be the (relative) displacement from center (not absolute coordinates)
    public void setControlPointAtIndex(Point2D p, int index) {
        if ((index >= 0) && (index < controlPoints.size())) {
            controlPoints.set(index, p);
        } else if (index == controlPoints.size()) {
            controlPoints.add(p);
        }
    }

    public void scaleCoords(float xFactor, float yFactor) {
        Point2D pt = new Point2D.Double(Math.round(center.getX() * xFactor),
                Math.round(center.getY() * yFactor));
        center = pt;
        for (Point2D cp : controlPoints) {
            pt = new Point2D.Double(Math.round(cp.getX() * xFactor),
                    Math.round(cp.getY() * yFactor));
            cp.setLocation(pt);
        }
    }

    // initialization instance variables (used when loading a LayoutEditor)
    public String connectAName = "";
    public String connectBName = "";
    public String tBlockName = "";

    /**
     * Initialization method The above variables are initialized by
     * PositionablePointXml, then the following method is called after the
     * entire LayoutEditor is loaded to set the specific TrackSegment objects.
     */
    public void setObjects(LayoutEditor p) {
        connectA = p.getFinder().findTrackSegmentByName(connectAName);
        connectB = p.getFinder().findTrackSegmentByName(connectBName);
        if (tBlockName.length() > 0) {
            block = p.getLayoutBlock(tBlockName);
            if (block != null) {
                blockName = tBlockName;
            } else {
                log.error("bad blockname '" + tBlockName + "' in LayoutFlex " + ident);
            }
        }
    }

    JPopupMenu popup = null;
    LayoutEditorTools tools = null;

    /**
     * Display popup menu for information and editing
     */
    protected void showPopUp(MouseEvent e, boolean isEditable) {
        if (popup != null) {
            popup.removeAll();
        } else {
            popup = new JPopupMenu();
        }
        if (isEditable) {
            popup.add(rb.getString("LayoutFlex"));
            boolean blockAssigned = false;
            if ((blockName == null) || (blockName.equals(""))) {
                popup.add(Bundle.getMessage("NoBlockX", 1));
            } else {
                popup.add(Bundle.getMessage("Block_ID", 1) + ": " + getLayoutBlock().getID());
                blockAssigned = true;
            }

            if (hidden) {
                popup.add(rb.getString("Hidden"));
            } else {
                popup.add(rb.getString("NotHidden"));
            }

            popup.add(new JSeparator(JSeparator.HORIZONTAL));
            popup.add(new AbstractAction(Bundle.getMessage("ButtonEdit")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editLayoutFlex(instance);
                }
            });
            popup.add(new AbstractAction(Bundle.getMessage("ButtonDelete")) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (layoutEditor.removeLayoutFlex(instance)) {
                        // Returned true if user did not cancel
                        remove();
                        dispose();
                    }
                }
            });

            if ((!blockName.equals("")) && (jmri.InstanceManager.getDefault(LayoutBlockManager.class).isAdvancedRoutingEnabled())) {
               popup.add(new AbstractAction(rb.getString("ViewBlockRouting")) {
                   @Override
                   public void actionPerformed(ActionEvent e) {
                       AbstractAction routeTableAction = new LayoutBlockRouteTableAction("ViewRouting", getLayoutBlock());
                       routeTableAction.actionPerformed(e);
                   }
               });
           }

            layoutEditor.setShowAlignmentMenu(popup);
            popup.show(e.getComponent(), e.getX(), e.getY());
        } else if (!viewAdditionalMenu.isEmpty()) {
            setAdditionalViewPopUpMenu(popup);
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    // variables for Edit Layout Flex pane
    private JmriJFrame editLayoutFlexFrame = null;
    private JCheckBox hiddenBox = new JCheckBox(rb.getString("HideFlex"));

    private JmriBeanComboBox blockNameComboBox = new JmriBeanComboBox(
            InstanceManager.getDefault(BlockManager.class), null, JmriBeanComboBox.DisplayOptions.DISPLAYNAME);
    private JButton flexEditDone;
    private JButton flexEditCancel;
    private JButton flexEditBlock;
    private boolean editOpen = false;
    private boolean needsRedraw = false;
    private boolean needsBlockUpdate = false;

    /**
     * Edit a Level Crossing
     */
    protected void editLayoutFlex(LayoutFlex o) {
        if (editOpen) {
            editLayoutFlexFrame.setVisible(true);
            return;
        }
        // Initialize if needed
        if (editLayoutFlexFrame == null) {
            editLayoutFlexFrame = new JmriJFrame(rb.getString("EditXing"), false, true);
            editLayoutFlexFrame.addHelpMenu("package.jmri.jmrit.display.EditLayoutFlex", true);
            editLayoutFlexFrame.setLocation(50, 30);
            Container contentPane = editLayoutFlexFrame.getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

            JPanel panel33 = new JPanel();
            panel33.setLayout(new FlowLayout());
            hiddenBox.setToolTipText(rb.getString("HiddenToolTip"));
            panel33.add(hiddenBox);
            contentPane.add(panel33);

            // setup block name
            JPanel panel1 = new JPanel();
            panel1.setLayout(new FlowLayout());
            JLabel blockNameLabel = new JLabel(Bundle.getMessage("Block_ID", 1));
            panel1.add(blockNameLabel);
            panel1.add(blockNameComboBox);
            layoutEditor.setupComboBox(blockNameComboBox, false, true);
            blockNameComboBox.setToolTipText(rb.getString("EditBlockNameHint"));
            contentPane.add(panel1);

            // set up Edit Block buttons
            JPanel panel4 = new JPanel();
            panel4.setLayout(new FlowLayout());
            // Edit Block
            panel4.add(flexEditBlock = new JButton(Bundle.getMessage("EditBlock", 1)));
            flexEditBlock.addActionListener((ActionEvent e) -> {
                flexEditBlockPressed(e);
            });
            flexEditBlock.setToolTipText(Bundle.getMessage("EditBlockHint", "")); // empty value for block

            contentPane.add(panel4);

            // set up Done and Cancel buttons
            JPanel panel5 = new JPanel();
            panel5.setLayout(new FlowLayout());
            panel5.add(flexEditDone = new JButton(Bundle.getMessage("ButtonDone")));
            flexEditDone.addActionListener((ActionEvent e) -> {
                flexEditDonePressed(e);
            });
            flexEditDone.setToolTipText(Bundle.getMessage("DoneHint", Bundle.getMessage("ButtonDone")));

            // make this button the default button (return or enter activates)
            // Note: We have to invoke this later because we don't currently have a root pane
            SwingUtilities.invokeLater(() -> {
                JRootPane rootPane = SwingUtilities.getRootPane(flexEditDone);
                rootPane.setDefaultButton(flexEditDone);
            });

            // Cancel
            panel5.add(flexEditCancel = new JButton(Bundle.getMessage("ButtonCancel")));
            flexEditCancel.addActionListener((ActionEvent e) -> {
                flexEditCancelPressed(e);
            });
            flexEditCancel.setToolTipText(Bundle.getMessage("CancelHint", Bundle.getMessage("ButtonCancel")));
            contentPane.add(panel5);
        }

        hiddenBox.setSelected(hidden);

        // Set up for Edit
        blockNameComboBox.getEditor().setItem(blockName);
        editLayoutFlexFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                flexEditCancelPressed(null);
            }
        });
        editLayoutFlexFrame.pack();
        editLayoutFlexFrame.setVisible(true);
        editOpen = true;
        needsBlockUpdate = false;
    }

    void flexEditBlockPressed(ActionEvent a) {
        // check if a block name has been entered
        String newName = blockNameComboBox.getUserName();
        if (!blockName.equals(newName)) {
            // block has changed, if old block exists, decrement use
            if (block != null) {
                block.decrementUse();
            }
            // get new block, or null if block has been removed
            blockName = newName;
            if (blockName.length() > 0) {
                try {
                    block = layoutEditor.provideLayoutBlock(blockName);
                } catch (IllegalArgumentException ex) {
                    blockName = "";
                    blockNameComboBox.getEditor().setItem("");
                    blockNameComboBox.setSelectedIndex(-1);
                }
            } else {
                block = null;
                blockName = "";
            }
            needsRedraw = true;
            layoutEditor.auxTools.setBlockConnectivityChanged();
            needsBlockUpdate = true;
        }
        // check if a block exists to edit
        if (block == null) {
            JOptionPane.showMessageDialog(editLayoutFlexFrame,
                    rb.getString("Error1"),
                    Bundle.getMessage("ErrorTitle"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        block.editLayoutBlock(editLayoutFlexFrame);
        needsRedraw = true;
    }

    void flexEditDonePressed(ActionEvent a) {
        // check if Blocks changed
        String newName = blockNameComboBox.getUserName();
        if (!blockName.equals(newName)) {
            // block has changed, if old block exists, decrement use
            if (block != null) {
                block.decrementUse();
            }
            // get new block, or null if block has been removed
            blockName = newName;
            if (blockName.length() > 0) {
                try {
                    block = layoutEditor.provideLayoutBlock(blockName);
                } catch (IllegalArgumentException ex) {
                    blockName = "";
                    blockNameComboBox.getEditor().setItem("");
                    blockNameComboBox.setSelectedIndex(-1);
                }
            } else {
                block = null;
                blockName = "";
            }
            needsRedraw = true;
            layoutEditor.auxTools.setBlockConnectivityChanged();
            needsBlockUpdate = true;
        }

        // set hidden
        boolean oldHidden = hidden;
        hidden = hiddenBox.isSelected();
        if (oldHidden != hidden) {
            needsRedraw = true;
        }

        editOpen = false;
        editLayoutFlexFrame.setVisible(false);
        editLayoutFlexFrame.dispose();
        editLayoutFlexFrame = null;
        if (needsBlockUpdate) {
            updateBlockInfo();
        }
        if (needsRedraw) {
            layoutEditor.redrawPanel();
            layoutEditor.setDirty();
        }
    }

    void flexEditCancelPressed(ActionEvent a) {
        editOpen = false;
        editLayoutFlexFrame.setVisible(false);
        editLayoutFlexFrame.dispose();
        editLayoutFlexFrame = null;
        if (needsBlockUpdate) {
            updateBlockInfo();
        }
        if (needsRedraw) {
            layoutEditor.redrawPanel();
            layoutEditor.setDirty();
        }
    }

    /**
     * Clean up when this object is no longer needed. Should not be called while
     * the object is still displayed; see remove()
     */
    void dispose() {
        if (popup != null) {
            popup.removeAll();
        }
        popup = null;
    }

    /**
     * Removes this object from display and persistance
     */
    void remove() {
        // remove from persistance by flagging inactive
        active = false;
    }

    boolean active = true;

    /**
     * "active" means that the object is still displayed, and should be stored.
     */
    public boolean isActive() {
        return active;
    }

    ArrayList<JMenuItem> editAdditionalMenu = new ArrayList<JMenuItem>(0);
    ArrayList<JMenuItem> viewAdditionalMenu = new ArrayList<JMenuItem>(0);

    public void addEditPopUpMenu(JMenuItem menu) {
        if (!editAdditionalMenu.contains(menu)) {
            editAdditionalMenu.add(menu);
        }
    }

    public void addViewPopUpMenu(JMenuItem menu) {
        if (!viewAdditionalMenu.contains(menu)) {
            viewAdditionalMenu.add(menu);
        }
    }

    public void setAdditionalEditPopUpMenu(JPopupMenu popup) {
        if (editAdditionalMenu.isEmpty()) {
            return;
        }
        popup.addSeparator();
        for (JMenuItem mi : editAdditionalMenu) {
            popup.add(mi);
        }
    }

    public void setAdditionalViewPopUpMenu(JPopupMenu popup) {
        if (viewAdditionalMenu.isEmpty()) {
            return;
        }
        popup.addSeparator();
        for (JMenuItem mi : viewAdditionalMenu) {
            popup.add(mi);
        }
    }

    public void draw(Graphics2D g2) {
        // set color - check for a block
        LayoutBlock b = getLayoutBlock();
        if (b != null) {
            g2.setColor(b.getBlockColor());
        } else {
            g2.setColor(defaultTrackColor);
        }
        // set track width for block
        layoutEditor.setTrackStrokeWidth(g2, isMainline());
        // draw AC segment
        //TODO: add code to draw bezier curve using the control points
        g2.draw(new Line2D.Double(getCoordsA(), getCoordsB()));
    }   // draw(Graphics2D g2)

    private final static Logger log = LoggerFactory.getLogger(LayoutFlex.class.getName());
}
