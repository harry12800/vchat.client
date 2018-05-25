package cn.harry12800.vchat.components.message;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import cn.harry12800.vchat.app.Launcher;
import cn.harry12800.vchat.components.Colors;
import cn.harry12800.vchat.components.RCMenuItemUI;
import cn.harry12800.vchat.components.SizeAutoAdjustTextArea;
import cn.harry12800.vchat.db.model.FileAttachment;
import cn.harry12800.vchat.db.service.FileAttachmentService;
import cn.harry12800.vchat.entity.MessageItem;
import cn.harry12800.vchat.frames.MainFrame;
import cn.harry12800.vchat.panels.ChatPanel;
import cn.harry12800.vchat.utils.ClipboardUtil;
import cn.harry12800.vchat.utils.FileCache;
import cn.harry12800.vchat.utils.ImageCache;

/**
 * Created by harry12800 on 2017/6/5.
 */
@SuppressWarnings("serial")
public class MessagePopupMenu extends JPopupMenu
{
    private int messageType;
    private ImageCache imageCache = new ImageCache();
    private FileCache fileCache = new FileCache();
    private FileAttachmentService fileAttachmentService = Launcher.fileAttachmentService;

    public MessagePopupMenu()
    {
        initMenuItem();
    }

    private void initMenuItem()
    {
        JMenuItem item1 = new JMenuItem("复制");
        JMenuItem item2 = new JMenuItem("删除");
        JMenuItem item3 = new JMenuItem("转发");

        item1.setUI(new RCMenuItemUI());
        item1.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                switch (messageType)
                {
                    case MessageItem.RIGHT_TEXT:
                    case MessageItem.LEFT_TEXT:
                    {
                        SizeAutoAdjustTextArea textArea = (SizeAutoAdjustTextArea) getInvoker();
                        String text = textArea.getSelectedText() == null ? textArea.getText() : textArea.getSelectedText();
                        if (text != null)
                        {
                            ClipboardUtil.copyString(text);
                        }
                        break;
                    }
                    case (MessageItem.RIGHT_IMAGE):
                    case (MessageItem.LEFT_IMAGE):
                    {
                        MessageImageLabel imageLabel = (MessageImageLabel) getInvoker();
                        Object obj = imageLabel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String url = (String) map.get("url");
                            imageCache.requestOriginalAsynchronously(id, url, new ImageCache.ImageCacheRequestListener()
                            {
                                @Override
                                public void onSuccess(ImageIcon icon, String path)
                                {
                                    if (path != null && !path.isEmpty())
                                    {
                                        ClipboardUtil.copyImage(path);
                                    }
                                    else
                                    {
                                        System.out.println("图片不存在，复制失败");
                                    }
                                }

                                @Override
                                public void onFailed(String why)
                                {
                                    System.out.println("图片不存在，复制失败");
                                }
                            });
                        }
                        break;
                    }

                    case (MessageItem.RIGHT_ATTACHMENT):
                    case (MessageItem.LEFT_ATTACHMENT):
                    {
                        AttachmentPanel attachmentPanel = (AttachmentPanel) getInvoker();
                        Object obj = attachmentPanel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            String id = (String) map.get("attachmentId");
                            String name = (String) map.get("name");

                            String path = fileCache.tryGetFileCache(id, name);
                            if (path != null && !path.isEmpty())
                            {
                                ClipboardUtil.copyFile(path);
                            }
                            else
                            {
                                FileAttachment attachment = fileAttachmentService.findById(id);
                                if (attachment == null)
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }

                                String link = attachment.getLink();
                                if (link.startsWith("/file-upload"))
                                {
                                    JOptionPane.showMessageDialog(MainFrame.getContext(), "请先下载文件", "请先下载文件", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                else
                                {
                                    File file = new File(link);
                                    if (!file.exists())
                                    {
                                        JOptionPane.showMessageDialog(MainFrame.getContext(), "文件不存在，可能已被删除", "文件不存在", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    ClipboardUtil.copyFile(link);
                                }
                            }
                        }
                        break;
                    }
                }

            }
        });


        item2.setUI(new RCMenuItemUI());
        item2.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String messageId = null;
                switch (messageType)
                {
                    case MessageItem.RIGHT_TEXT:
                    case MessageItem.LEFT_TEXT:
                    {
                        SizeAutoAdjustTextArea textArea = (SizeAutoAdjustTextArea) getInvoker();
                        messageId = textArea.getTag().toString();
                        break;
                    }
                    case (MessageItem.RIGHT_IMAGE):
                    case (MessageItem.LEFT_IMAGE):
                    {
                        MessageImageLabel imageLabel = (MessageImageLabel) getInvoker();
                        Object obj = imageLabel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            messageId = (String) map.get("messageId");
                        }
                        break;
                    }
                    case (MessageItem.RIGHT_ATTACHMENT):
                    case (MessageItem.LEFT_ATTACHMENT):
                    {
                        AttachmentPanel attachmentPanel = (AttachmentPanel) getInvoker();
                        Object obj = attachmentPanel.getTag();
                        if (obj != null)
                        {
                            Map map = (Map) obj;
                            messageId = (String) map.get("messageId");
                        }
                        break;
                    }
                }

                if (messageId != null && !messageId.isEmpty())
                {
                    ChatPanel.getContext().deleteMessage(messageId);
                }
            }
        });

        item3.setUI(new RCMenuItemUI());
        item3.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("转发");
            }
        });

        this.add(item1);
        this.add(item2);
        //this.add(item3);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }

    @Override
    public void show(Component invoker, int x, int y)
    {
        throw new RuntimeException("此方法不会弹出菜单，请调用 show(Component invoker, int x, int y, int messageType) ");
        //super.show(invoker, x, y);
    }

    public void show(Component invoker, int x, int y, int messageType)
    {
        this.messageType = messageType;
        super.show(invoker, x, y);
    }
}
