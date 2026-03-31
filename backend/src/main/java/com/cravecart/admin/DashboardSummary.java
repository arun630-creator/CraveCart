package com.cravecart.admin;

public class DashboardSummary {

    private long productCount;
    private long categoryCount;
    private long orderCount;
    private double totalRevenue;
    private long availableProductCount;
    private long unavailableProductCount;

    public long getProductCount() {
        return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }

    public long getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(long categoryCount) {
        this.categoryCount = categoryCount;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getAvailableProductCount() {
        return availableProductCount;
    }

    public void setAvailableProductCount(long availableProductCount) {
        this.availableProductCount = availableProductCount;
    }

    public long getUnavailableProductCount() {
        return unavailableProductCount;
    }

    public void setUnavailableProductCount(long unavailableProductCount) {
        this.unavailableProductCount = unavailableProductCount;
    }
}
