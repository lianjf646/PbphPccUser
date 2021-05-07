package com.pbph.pcc.fragment;
import io.rong.imkit.fragment.ConversationFragment;

/**
 *  会话 Fragment 继承自ConversationFragment
 *  onResendItemClick: 重发按钮点击事件. 如果返回 false,走默认流程,如果返回 true,走自定义流程
 *  onReadReceiptStateClick: 已读回执详情的点击事件.
 *  如果不需要重写 onResendItemClick 和 onReadReceiptStateClick ,可以不必定义此类,直接集成 ConversationFragment 就可以了
 *  Created by Yuejunhong on 2016/10/10.
 */
public class ConversationFragmentEx extends ConversationFragment {
    @Override
    public boolean onResendItemClick(io.rong.imlib.model.Message message) {

        return false;
    }

    @Override
    public void onReadReceiptStateClick(io.rong.imlib.model.Message message) {

    }
}
