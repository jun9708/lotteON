package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdOptDetail is a Querydsl query type for ProdOptDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdOptDetail extends EntityPathBase<ProdOptDetail> {

    private static final long serialVersionUID = 2049922826L;

    public static final QProdOptDetail prodOptDetail = new QProdOptDetail("prodOptDetail");

    public final NumberPath<Integer> optDetailNo = createNumber("optDetailNo", Integer.class);

    public final NumberPath<Integer> optNo1 = createNumber("optNo1", Integer.class);

    public final NumberPath<Integer> optNo2 = createNumber("optNo2", Integer.class);

    public final NumberPath<Integer> optNo3 = createNumber("optNo3", Integer.class);

    public final NumberPath<Integer> optPrice = createNumber("optPrice", Integer.class);

    public final NumberPath<Integer> optStock = createNumber("optStock", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public QProdOptDetail(String variable) {
        super(ProdOptDetail.class, forVariable(variable));
    }

    public QProdOptDetail(Path<? extends ProdOptDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdOptDetail(PathMetadata metadata) {
        super(ProdOptDetail.class, metadata);
    }

}

