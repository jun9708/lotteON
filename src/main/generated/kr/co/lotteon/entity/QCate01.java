package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate01 is a Querydsl query type for Cate01
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate01 extends EntityPathBase<Cate01> {

    private static final long serialVersionUID = 2119830643L;

    public static final QCate01 cate01 = new QCate01("cate01");

    public final StringPath cate01Name = createString("cate01Name");

    public final StringPath cate01No = createString("cate01No");

    public QCate01(String variable) {
        super(Cate01.class, forVariable(variable));
    }

    public QCate01(Path<? extends Cate01> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate01(PathMetadata metadata) {
        super(Cate01.class, metadata);
    }

}

