package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupons is a Querydsl query type for Coupons
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupons extends EntityPathBase<Coupons> {

    private static final long serialVersionUID = 1692362378L;

    public static final QCoupons coupons = new QCoupons("coupons");

    public final NumberPath<Integer> cpDcPrice = createNumber("cpDcPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> cpEndDate = createDateTime("cpEndDate", java.time.LocalDateTime.class);

    public final StringPath cpInfo = createString("cpInfo");

    public final NumberPath<Integer> cpMinPrice = createNumber("cpMinPrice", Integer.class);

    public final StringPath cpName = createString("cpName");

    public final NumberPath<Integer> cpNo = createNumber("cpNo", Integer.class);

    public final StringPath cpStatus = createString("cpStatus");

    public QCoupons(String variable) {
        super(Coupons.class, forVariable(variable));
    }

    public QCoupons(Path<? extends Coupons> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupons(PathMetadata metadata) {
        super(Coupons.class, metadata);
    }

}

