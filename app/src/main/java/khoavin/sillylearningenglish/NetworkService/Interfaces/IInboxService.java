package khoavin.sillylearningenglish.NetworkService.Interfaces;

import android.content.Context;

import java.util.ArrayList;

import khoavin.sillylearningenglish.NetworkService.NetworkModels.AttachItem;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.ErrorCode;
import khoavin.sillylearningenglish.NetworkService.NetworkModels.Inbox;

public interface IInboxService {

    /**
     * Get the inbox items
     *
     * @param user_id The user's identifier
     */
    void GetInboxItems(String user_id, Context context, IVolleyService volleyService, IVolleyResponse<ArrayList<Inbox>> receiver);

    /**
     * Rate mail
     *
     * @param user_id The user's identifier
     * @param mail_id The mail's identifier
     */
    void RateMail(String user_id, int mail_id, Context context, IVolleyService volleyService, IVolleyResponse<ErrorCode> receiver);

    /**
     * Rate mail
     *
     * @param user_id The user's identifier
     * @param mail_id The mail's identifier
     */
    void RemoveMail(String user_id, int mail_id, Context context, IVolleyService volleyService, IVolleyResponse<ErrorCode> receiver);

    /**
     * Mask mail as opened
     *
     * @param user_id The user's Identifier
     * @param mail_id The mail's Identifier
     */
    void MaskAsOpened(String user_id, int mail_id, Context context, IVolleyService volleyService, IVolleyResponse<ErrorCode> receiver);

    /**
     * Claim mail's reward
     *
     * @param user_id The user's identifier
     * @param mail_id The mail's identifier
     */
    void ClaimReward(String user_id, int mail_id, Context context, IVolleyService volleyService, IVolleyResponse<ErrorCode> receiver);

    /**
     * Get current inbox items
     *
     * @return
     */
    ArrayList<Inbox> GetCurrentInboxItem();

    /**
     * Remove item , this function just remove item from local inbox items
     */
    ArrayList<Inbox> RemoveItem(int mail_identifier);

    /**
     * Remove item, this function just remove item from local inbox items
     *
     * @param mail_item
     */
    ArrayList<Inbox> RemoveItem(Inbox mail_item);

    /**
     * Gets all inbox item of mail
     * @param user_id The user's identifier
     * @param mail_id The mail's identifier
     * @return
     * The list of all attach items
     */
    void GetAttachItems(String user_id, int mail_id, Context context, IVolleyService volleyService, IVolleyResponse<ArrayList<AttachItem>> receiver);

    /**
     * Remove item from inbox adapter.
     * @param mail_id The mail id.
     */
    void RemoveItemFormView(int mail_id);

    /**
     * Gets value indicate when inbox items has changed.
     * @return true if inbox items has changed otherwise return false.
     */
    boolean IsInboxUpdated();

    /**
     * Update inbox item.
     */
    void UpdateInboxItem(Inbox item);

    /**
     * Call this function after refresh inbox items.
     */
    void SetInboxToUpToDate();

}
