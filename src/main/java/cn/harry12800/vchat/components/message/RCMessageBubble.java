package cn.harry12800.vchat.components.message;

import javax.swing.*;
import java.awt.event.MouseListener;

/**
 * Created by harry12800 on 27/06/2017.
 */
public interface RCMessageBubble
{
    void addMouseListener(MouseListener l);

    void setBackgroundIcon(Icon icon);

    NinePatchImageIcon getBackgroundNormalIcon();

    NinePatchImageIcon getBackgroundActiveIcon();
}
