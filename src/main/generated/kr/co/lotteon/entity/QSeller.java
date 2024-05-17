package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeller is a Querydsl query type for Seller
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeller extends EntityPathBase<Seller> {

    private static final long serialVersionUID = -1713606046L;

    public static final QSeller seller = new QSeller("seller");

    public final StringPath businessNum = createString("businessNum");

    public final StringPath company = createString("company");

    public final StringPath fax = createString("fax");

    public final StringPath licenseNum = createString("licenseNum");

    public final StringPath sellerGrade = createString("sellerGrade");

    public final StringPath sellerHp = createString("sellerHp");

    public final StringPath sellerName = createString("sellerName");

    public final StringPath sellerNo = createString("sellerNo");

    public final StringPath userId = createString("userId");

    public QSeller(String variable) {
        super(Seller.class, forVariable(variable));
    }

    public QSeller(Path<? extends Seller> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeller(PathMetadata metadata) {
        super(Seller.class, metadata);
    }

}

