package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderDetail is a Querydsl query type for OrderDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDetail extends EntityPathBase<OrderDetail> {

    private static final long serialVersionUID = 1630112828L;

    public static final QOrderDetail orderDetail = new QOrderDetail("orderDetail");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final DatePath<java.time.LocalDate> detailDate = createDate("detailDate", java.time.LocalDate.class);

    public final NumberPath<Integer> detailNo = createNumber("detailNo", Integer.class);

    public final NumberPath<Integer> detailPoint = createNumber("detailPoint", Integer.class);

    public final NumberPath<Integer> detailPrice = createNumber("detailPrice", Integer.class);

    public final StringPath detailStatus = createString("detailStatus");

    public final NumberPath<Integer> optNo = createNumber("optNo", Integer.class);

    public final NumberPath<Integer> orderNo = createNumber("orderNo", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath prodSeller = createString("prodSeller");

    public QOrderDetail(String variable) {
        super(OrderDetail.class, forVariable(variable));
    }

    public QOrderDetail(Path<? extends OrderDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderDetail(PathMetadata metadata) {
        super(OrderDetail.class, metadata);
    }

}

