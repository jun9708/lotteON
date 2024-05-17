package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCartProduct is a Querydsl query type for CartProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartProduct extends EntityPathBase<CartProduct> {

    private static final long serialVersionUID = -1477355348L;

    public static final QCartProduct cartProduct = new QCartProduct("cartProduct");

    public final NumberPath<Integer> cartNo = createNumber("cartNo", Integer.class);

    public final NumberPath<Integer> cartProdNo = createNumber("cartProdNo", Integer.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Integer> optNo = createNumber("optNo", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public QCartProduct(String variable) {
        super(CartProduct.class, forVariable(variable));
    }

    public QCartProduct(Path<? extends CartProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCartProduct(PathMetadata metadata) {
        super(CartProduct.class, metadata);
    }

}

