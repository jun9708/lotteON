package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserCoupon is a Querydsl query type for UserCoupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCoupon extends EntityPathBase<UserCoupon> {

    private static final long serialVersionUID = -934386380L;

    public static final QUserCoupon userCoupon = new QUserCoupon("userCoupon");

    public final NumberPath<Integer> couponId = createNumber("couponId", Integer.class);

    public final NumberPath<Integer> cpNo = createNumber("cpNo", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> ucpDate = createDateTime("ucpDate", java.time.LocalDateTime.class);

    public final StringPath ucpStatus = createString("ucpStatus");

    public final DateTimePath<java.time.LocalDateTime> ucpUseDate = createDateTime("ucpUseDate", java.time.LocalDateTime.class);

    public final StringPath userId = createString("userId");

    public QUserCoupon(String variable) {
        super(UserCoupon.class, forVariable(variable));
    }

    public QUserCoupon(Path<? extends UserCoupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserCoupon(PathMetadata metadata) {
        super(UserCoupon.class, metadata);
    }

}

