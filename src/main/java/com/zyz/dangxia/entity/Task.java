package com.zyz.dangxia.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "publisher")
    private int publisher;

    @Column(name = "executor")
    private int executor;

    @Column(name = "type")
    private int type;

    @Column(name = "publish_date")
    private Date publishDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "content")
    private String content;

    @Column(name = "require_verify")
    private int requireVerify;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }

    public int getExecutor() {
        return executor;
    }

    public void setExecutor(int executor) {
        this.executor = executor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRequireVerify() {
        return requireVerify;
    }

    public void setRequireVerify(int requireVerify) {
        this.requireVerify = requireVerify;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", publisher=" + publisher +
                ", executor=" + executor +
                ", type=" + type +
                ", publishDate=" + publishDate +
                ", endDate=" + endDate +
                ", content='" + content + '\'' +
                ", requireVerify=" + requireVerify +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", price=" + price +
                '}';
    }
}
