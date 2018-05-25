package cn.harry12800.vchat.components.message;

/**
 * 右侧文本聊天气泡
 */
@SuppressWarnings("serial")
public class RCRightTextMessageBubble extends RCTextMessageBubble
{
    public RCRightTextMessageBubble()
    {
        NinePatchImageIcon backgroundNormal = new NinePatchImageIcon(this.getClass().getResource("/image/right.9.png"));
        NinePatchImageIcon backgroundActive = new NinePatchImageIcon(this.getClass().getResource("/image/right_active.9.png"));
        setBackgroundNormalIcon(backgroundNormal);
        setBackgroundActiveIcon(backgroundActive);
        setBackgroundIcon(backgroundNormal);
    }
}
