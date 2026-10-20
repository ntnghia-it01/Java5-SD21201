package com.fpoly.java5.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;
	
	@Column(name = "total_amount", nullable = false)
	private int totalAmount;
	
	@Column(name = "status", nullable = false)
	private int status = 1;
//	1 => Chờ xác nhận đơn (Tự động khi đặt hàng)
//	2 => Cửa hàng đã xác nhận đơn (Do admin xác nhận) 
//	3 => Đang vận chuyển (Do admin xác nhận)
//	4 => Đã giao hàng (Do admin xác nhận)
//	5 => Xác hoàn thành (User xác nhận)
//	6 => Huỷ đơn
//		- Với phía user chỉ được huỷ khi đơn chờ xác nhận 
//		- Với phía admin sẽ được huỷ khi đơn chờ xác nhận hoặc đã xác nhận
	
//	Không được xoá đơn hàng (Dù cho đơn này đã huỷ)
	
	@Column(name = "shipping_name", nullable = false, columnDefinition = "NVARCHAR (100)")
	private String shippingName;
	
	@Column(name = "shipping_address", nullable = false, length = 20)
	private String shippingPhone;
	
	@Column(name = "shipping_address", nullable = false, columnDefinition = "NVARCHAR (255)")
	private String shippingAddress;
	
	@Column(name = "note", nullable = false, columnDefinition = "NVARCHAR (500)")
	private String note;
	
	@Column(name = "created_at", nullable = false, columnDefinition = "DATETIME2 (7)")
	private LocalDateTime createdDate = LocalDateTime.now(); // Ngày tạo đơn hàng
	
	@Column(name = "updated_at", columnDefinition = "DATETIME2 (7)")
	private LocalDateTime updatedDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity userEntity;
	
	@OneToMany(mappedBy = "orderEntity")
	private List<OrderItemEntity> orderItemEntities;
}


//CREATE TABLE [dbo].[orders] (
//	    [order_id]         INT             IDENTITY (1, 1) NOT NULL,
//	    [user_id]          INT             NOT NULL,
//	    [total_amount]     DECIMAL (18, 2) NOT NULL,
//	    [status]           INT             DEFAULT ((1)) NOT NULL,
//	    [shipping_name]    NVARCHAR (100)  NOT NULL,
//	    [shipping_phone]   VARCHAR (20)    NOT NULL,
//	    [shipping_address] NVARCHAR (255)  NOT NULL,
//	    [note]             NVARCHAR (500)  NULL,
//	    [created_at]       DATETIME2 (7)   DEFAULT (getdate()) NOT NULL,
//	    [updated_at]       DATETIME2 (7)   NULL,
//	    PRIMARY KEY CLUSTERED ([order_id] ASC),
//	    CONSTRAINT [ck_orders_status] CHECK ([status]=(5) OR [status]=(4) OR [status]=(3) OR [status]=(2) OR [status]=(1)),
//	    CONSTRAINT [ck_orders_total_amount] CHECK ([total_amount]>=(0)),
//	    CONSTRAINT [fk_orders_users] FOREIGN KEY ([user_id]) REFERENCES [dbo].[users] ([user_id])
//	);
//
