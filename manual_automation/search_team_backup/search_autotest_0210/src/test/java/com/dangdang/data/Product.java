package com.dangdang.data;

/**
 * 这是一个数据类。该类用来保存由搜索结果返回的xml中解析出来的Product节点内容。
 * 
 * @author zhangyamin
 */
public class Product {
	
	private String product_id; // 商品ID 主键
	private String product_medium; // 商品介质（商品类型标识）
	private String ebook_product_id; // 电子书ID
	private String paper_book_product_id; // 纸质书ID
	private String product_type; // 商品类型
	private String prmotion_id; // 促销id
	private String product_name; // 商品名
	private String price; // 原价
	private String dd_sale_price; // 当当卖价
	private String promotion_saleprice; // 促销价
	private String shop_id; // 店铺id，0是当当
	private String promotion_type; // 促销类型
	private String real_dd_sale_price; // unkown
	private String reduced_price; // unkown
	private String discount; // 折扣 TODO 源数据需改为整型
	private String collection_promo_id; // 类别促销ID
	private String cat_promo_end_date;  //集合促销结束时间
	private String cat_promo_start_date;//集合促销开始时间
	private String exclusive_begin_date;//手机专享价活动开始时间
	private String exclusive_end_date;//手机专享价结束时间
	private String exclusive_reduce_price;//手机专享价立省金额
	private String stype;
	private String sale_week; // 周销量
	private String sale_week_amt; // 周销售额
	private String last_input_date; // 最后更新日期
	private String publish_date; // 出版日期
	private String promo_start_time; // 促销开始时间
	private String promo_end_time; // 促销结束时间
	private String is_sold_out; // 是否售罄，1售罄不后续采购，0后续采购
	private String stock_status; // 库存状态(0:不可售1:全国可售2：区域可售)
	private String pre_sale; // 是否是预售商品，1是0否
	private String product_action_id; // 奥莱活动id
	private String action_end_date; // 奥莱活动结束时间
	private String action_start_date; // 奥莱活动开始时间
	private String is_publication; // 是否出版物。 1为出版物。2为非出版物
	private String Score; // 评分
	private String final_score;//最终评分
	private String cat_paths; // 分类
	private String muti_promo_type;
	private String muti_promo_start_time; // 促销开始时间
	private String Muti_promo_end_time; // 促销结束时间
	private String total_review_count;// 评论数量
	private String is_delete;  //是否删除
	
	

	/**
	 * @return the exclusive_begin_date
	 */
	public String getExclusive_begin_date() {
		return exclusive_begin_date;
	}

	/**
	 * @param exclusive_begin_date the exclusive_begin_date to set
	 */
	public void setExclusive_begin_date(String exclusive_begin_date) {
		this.exclusive_begin_date = exclusive_begin_date;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_medium() {
		return product_medium;
	}

	public void setProduct_medium(String product_medium) {
		this.product_medium = product_medium;
	}

	public String getEbook_Product_id() {
		return ebook_product_id;
	}

	public void setEbook_Product_id(String ebook_product_id) {
		this.ebook_product_id = ebook_product_id;
	}
	
	public String getPaper_Book_Product_id() {
		return paper_book_product_id;
	}

	public void setPaper_Book_Product_id(String paper_book_product_id) {
		this.paper_book_product_id = paper_book_product_id;
	}
	
	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getPrmotion_id() {
		return prmotion_id;
	}

	public void setPrmotion_id(String prmotion_id) {
		this.prmotion_id = prmotion_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDd_sale_price() {
		return dd_sale_price;
	}

	public void setDd_sale_price(String dd_sale_price) {
		this.dd_sale_price = dd_sale_price;
	}

	public String getPromotion_saleprice() {
		return promotion_saleprice;
	}

	public void setPromotion_saleprice(String promotion_saleprice) {
		this.promotion_saleprice = promotion_saleprice;
	}

	public String getShop_id() {
		return shop_id;
	}

	public void setShop_id(String shop_id) {
		this.shop_id = shop_id;
	}

	public String getPromotion_type() {
		return promotion_type;
	}

	public void setPromotion_type(String promotion_type) {
		this.promotion_type = promotion_type;
	}

	public String getReal_dd_sale_price() {
		return real_dd_sale_price;
	}

	public void setReal_dd_sale_price(String real_dd_sale_price) {
		this.real_dd_sale_price = real_dd_sale_price;
	}

	public String getReduced_price() {
		return reduced_price;
	}

	public void setReduced_price(String reduced_price) {
		this.reduced_price = reduced_price;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getCollection_promo_id() {
		return collection_promo_id;
	}

	public void setCollection_promo_id(String collection_promo_id) {
		this.collection_promo_id = collection_promo_id;
	}

	public String getSale_week() {
		return sale_week;
	}

	public void setSale_week(String sale_week) {
		this.sale_week = sale_week;
	}

	public String getSale_week_amt() {
		return sale_week_amt;
	}

	public void setSale_week_amt(String sale_week_amt) {
		this.sale_week_amt = sale_week_amt;
	}

	public String getLast_input_date() {
		return last_input_date;
	}

	public void setLast_input_date(String last_input_date) {
		this.last_input_date = last_input_date;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public String getPromo_start_time() {
		return promo_start_time;
	}

	public void setPromo_start_time(String promo_start_time) {
		this.promo_start_time = promo_start_time;
	}

	public String getPromo_end_time() {
		return promo_end_time;
	}

	public void setPromo_end_time(String promo_end_time) {
		this.promo_end_time = promo_end_time;
	}

	public String getIs_sold_out() {
		return is_sold_out;
	}

	public void setIs_sold_out(String is_sold_out) {
		this.is_sold_out = is_sold_out;
	}

	public String getStock_status() {
		return stock_status;
	}

	public void setStock_status(String stock_status) {
		this.stock_status = stock_status;
	}

	public String getPre_sale() {
		return pre_sale;
	}

	public void setPre_sale(String pre_sale) {
		this.pre_sale = pre_sale;
	}

	public String getProduct_action_id() {
		return product_action_id;
	}

	public void setProduct_action_id(String product_action_id) {
		this.product_action_id = product_action_id;
	}

	public String getAction_end_date() {
		return action_end_date;
	}

	public void setAction_end_date(String action_end_date) {
		this.action_end_date = action_end_date;
	}

	public String getAction_start_date() {
		return action_start_date;
	}

	public void setAction_start_date(String action_start_date) {
		this.action_start_date = action_start_date;
	}

	public String getIs_publication() {
		return is_publication;
	}

	public void setIs_publication(String is_publication) {
		this.is_publication = is_publication;
	}

	public String getScore() {
		return Score;
	}

	public void setScore(String score) {
		Score = score;
	}
	
	public String getFinalScore() {
		return final_score;
	}
	
	public void setFinalScore(String final_score) {
		this.final_score = final_score;
	}
	
	public String getCat_paths() {
		return cat_paths;
	}

	public void setCat_paths(String cat_paths) {
		this.cat_paths = cat_paths;
	}

	public String getMuti_promo_type() {
		return muti_promo_type;
	}

	public void setMuti_promo_type(String muti_promo_type) {
		this.muti_promo_type = muti_promo_type;
	}

	public String getMuti_promo_start_time() {
		return muti_promo_start_time;
	}

	public void setMuti_promo_start_time(String muti_promo_start_time) {
		this.muti_promo_start_time = muti_promo_start_time;
	}

	public String getMuti_promo_end_time() {
		return Muti_promo_end_time;
	}

	public void setMuti_promo_end_time(String muti_promo_end_time) {
		Muti_promo_end_time = muti_promo_end_time;
	}
	
	public String getTotal_review_count() {
		return total_review_count;
	}

	public void setTotal_review_count(String total_review_count) {
		this.total_review_count = total_review_count;
	}

	public String getStype() {
		return stype;
	}

	public void setStype(String stype) {
		this.stype = stype;
	}
	

	
	
	public String getCat_promo_end_date() {
		return cat_promo_end_date;
	}

	public void setCat_promo_end_date(String cat_promo_end_date) {
		this.cat_promo_end_date = cat_promo_end_date;
	}

	public String getCat_promo_start_date() {
		return cat_promo_start_date;
	}

	public void setCat_promo_start_date(String cat_promo_start_date) {
		this.cat_promo_start_date = cat_promo_start_date;
	}

	
	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	/**
	 * @return the exclusive_end_date
	 */
	public String getExclusive_end_date() {
		return exclusive_end_date;
	}

	/**
	 * @param exclusive_end_date the exclusive_end_date to set
	 */
	public void setExclusive_end_date(String exclusive_end_date) {
		this.exclusive_end_date = exclusive_end_date;
	}

	/**
	 * @return the exclusive_reduce_price
	 */
	public String getExclusive_reduce_price() {
		return exclusive_reduce_price;
	}

	/**
	 * @param exclusive_reduce_price the exclusive_reduce_price to set
	 */
	public void setExclusive_reduce_price(String exclusive_reduce_price) {
		this.exclusive_reduce_price = exclusive_reduce_price;
	}

}
