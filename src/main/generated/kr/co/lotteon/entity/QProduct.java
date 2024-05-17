package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 425002604L;

    public static final QProduct product = new QProduct("product");

    public final StringPath cateCode = createString("cateCode");

    public final StringPath prodBusinessType = createString("prodBusinessType");

    public final StringPath prodCompany = createString("prodCompany");

    public final NumberPath<Integer> prodDeliveryFee = createNumber("prodDeliveryFee", Integer.class);

    public final NumberPath<Integer> prodDiscount = createNumber("prodDiscount", Integer.class);

    public final NumberPath<Integer> prodHit = createNumber("prodHit", Integer.class);

    public final StringPath prodInfo = createString("prodInfo");

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath prodOrg = createString("prodOrg");

    public final NumberPath<Integer> prodPoint = createNumber("prodPoint", Integer.class);

    public final NumberPath<Integer> prodPrice = createNumber("prodPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> prodRdate = createDateTime("prodRdate", java.time.LocalDateTime.class);

    public final StringPath prodReceipt = createString("prodReceipt");

    public final StringPath prodSeller = createString("prodSeller");

    public final NumberPath<Integer> prodSold = createNumber("prodSold", Integer.class);

    public final StringPath prodStatus = createString("prodStatus");

    public final NumberPath<Integer> prodStock = createNumber("prodStock", Integer.class);

    public final StringPath prodTax = createString("prodTax");

    public final NumberPath<Integer> tReviewCount = createNumber("tReviewCount", Integer.class);

    public final NumberPath<Float> tReviewScore = createNumber("tReviewScore", Float.class);

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

