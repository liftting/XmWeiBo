package wm.xmwei.bean.base;

import java.util.List;

/**
 * this is data list bean ,封装了，因为loaderManager加载机制需要，
 * 每一个数据项提供统一的接口 DataItemDomain
 */
public abstract class DataListDomain<T extends DataItemDomain, K> {

    protected int total_number = 0;
    protected String previous_cursor = "0";
    protected String next_cursor = "0";

    public abstract int getSize();

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public abstract T getItem(int position);

    public abstract List<T> getItemList();

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public abstract void addNewData(K newValue);


}
