package cn.harry12800.vchat.panels;

import cn.harry12800.vchat.components.Colors;

import javax.swing.*;
import java.awt.*;

/**
 * Created by harry12800 on 17-5-30.
 */
public class CollectionsPanel extends ParentAvailablePanel
{
    private JLabel tipLabel;

    public CollectionsPanel(JPanel parent)
    {
        super(parent);

        initComponents();
        initView();
    }

    private void initComponents()
    {
        tipLabel = new JLabel("暂无收藏");
        tipLabel.setForeground(Colors.FONT_GRAY);
    }

    private void initView()
    {
        this.setBackground(Colors.DARK);
        setLayout(new FlowLayout());
        add(tipLabel);
    }
}
