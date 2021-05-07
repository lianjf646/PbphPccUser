package com.pbph.pcc.rong;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;


public class SealExtensionModule extends DefaultExtensionModule {
    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
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
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
//        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        List<IPluginModule> pluginModules = new ArrayList<>();
//        if (conversationType.equals(Conversation.ConversationType.PRIVATE)
//                || conversationType.equals(Conversation.ConversationType.GROUP)
//                ) {
////            pluginModules.add(ContactCardPlugin.getInstance());
////            pluginModules.add(new ImagePlugin());
//
//        } else {
//
//    }
        pluginModules.add(new ImagePlugin());

        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }


    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule());
            }
        }
    }
}
