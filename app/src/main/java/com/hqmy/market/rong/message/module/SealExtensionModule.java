package com.hqmy.market.rong.message.module;

import com.hqmy.market.rong.model.MyImagePlugin;
import com.hqmy.market.rong.model.RedPacketPlugin;
import java.util.ArrayList;
import java.util.List;
import io.rong.callkit.AudioPlugin;
import io.rong.callkit.VideoPlugin;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class SealExtensionModule extends DefaultExtensionModule {

    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    public void onConnect(String token) {
        super.onConnect(token);
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);
    }

    @Override
    public void onDetachedFromExtension() {
        super.onDetachedFromExtension();
    }

    @Override
    public void onReceivedMessage(Message message) {
        super.onReceivedMessage(message);
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        IPluginModule image = new MyImagePlugin();
        pluginModuleList.add(image);
        IPluginModule file = new FilePlugin();
        pluginModuleList.add(file);
//      客户要求再放开实时位置
        IPluginModule locationPlugin = new DefaultLocationPlugin(); //地理位置
//      RealTimeLocationPlugin locationPlugin = new RealTimeLocationPlugin(); //实时位置
//      CombineLocationPlugin locationPlugin = new CombineLocationPlugin(); //地理位置和实时位置聚合
        pluginModuleList.add(locationPlugin);
        IPluginModule redPacket = new RedPacketPlugin();
        pluginModuleList.add(redPacket);
        AudioPlugin audio = new AudioPlugin();
        pluginModuleList.add(audio);
        VideoPlugin video = new VideoPlugin();
        pluginModuleList.add(video);
        return pluginModuleList;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }
}
