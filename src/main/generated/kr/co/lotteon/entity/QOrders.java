package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = -1816361528L;

    public static final QOrders orders = new QOrders("orders");

    public final StringPath orderAddr = createString("orderAddr");

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final StringPath orderHp = createString("orderHp");

    public final StringPath orderMemo = createString("orderMemo");

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final StringPath orderPay = createString("orderPay");

    public final NumberPath<Integer> orderPrice = createNumber("orderPrice", Integer.class);

    public final StringPath orderReceiver = createString("orderReceiver");

    public final StringPath orderStatus = createString("orderStatus");

    public final StringPath userId = createString("userId");

    public final NumberPath<Integer> userUsedPoint = createNumber("userUsedPoint", Integer.class);

    public QOrders(String variable) {
        super(Orders.class, forVariable(variable));
    }

    public QOrders(Path<? extends Orders> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrders(PathMetadata metadata) {
        super(Orders.class, metadata);
    }

}

