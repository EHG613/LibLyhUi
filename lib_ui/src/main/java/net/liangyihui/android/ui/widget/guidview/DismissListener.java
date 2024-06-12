/*
 *  DismissListener.java, 2022-09-28
 *  Copyright © 2015-2022  Liangyihui. All rights reserved.
 */

package net.liangyihui.android.ui.widget.guidview;

/**
 * Listener for dismissing of one GuideCaseView
 */
public interface DismissListener {
    /**
     * is called when a {@link GuideCaseView} is dismissed
     *
     * @param id the show once id of the dismissed view
     */
    void onDismiss(String id);
    /**
     * is called when a {@link GuideCaseView} is skipped because of it's show once id
     *
     * @param id the show once id of the dismissed view
     */
    void onSkipped(String id);
}
