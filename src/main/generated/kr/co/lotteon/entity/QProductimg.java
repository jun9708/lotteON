package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductimg is a Querydsl query type for Productimg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductimg extends EntityPathBase<Productimg> {

    private static final long serialVersionUID = -310908457L;

    public static final QProductimg productimg = new QProductimg("productimg");

    public final NumberPath<Integer> imgNo = createNumber("imgNo", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath thumb190 = createString("thumb190");

    public final StringPath thumb230 = createString("thumb230");

    public final StringPath thumb456 = createString("thumb456");

    public final StringPath thumb940 = createString("thumb940");

    public QProductimg(String variable) {
        super(Productimg.class, forVariable(variable));
    }

    public QProductimg(Path<? extends Productimg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductimg(PathMetadata metadata) {
        super(Productimg.class, metadata);
    }

}

