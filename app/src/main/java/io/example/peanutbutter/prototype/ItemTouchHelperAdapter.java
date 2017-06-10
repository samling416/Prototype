package io.example.peanutbutter.prototype;

/**
 * Created by Samuel on 6/06/2017.
 */

public interface ItemTouchHelperAdapter {

    /** Called when an item has been dragged far enough to trigger a move. Called every time an item
     * is shifted.
     *
     * @Param fromPosition The start position of the moved item.
     * @Param toPosition: The end position of the moved item.
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /** Called when an item has been dismissed by a swipe.
     *
     * @param position: The position of the item dismissed.
     */
    void onItemDismiss(int position);

    /** Called when an item has been swiped right.
     *
     * @param position
     */

    void onItemSwiped(int position);

}
