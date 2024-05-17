package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdOption is a Querydsl query type for ProdOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdOption extends EntityPathBase<ProdOption> {

    private static final long serialVersionUID = -1386810353L;

    public static final QProdOption prodOption = new QProdOption("prodOption");

    public final StringPath optName = createString("optName");

    public final NumberPath<Integer> optNo = createNumber("optNo", Integer.class);

    public final StringPath optValue = createString("optValue");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public QProdOption(String variable) {
        super(ProdOption.class, forVariable(variable));
    }

    public QProdOption(Path<? extends ProdOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdOption(PathMetadata metadata) {
        super(ProdOption.class, metadata);
    }

}

