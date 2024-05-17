package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddr is a Querydsl query type for Addr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAddr extends EntityPathBase<Addr> {

    private static final long serialVersionUID = 900471700L;

    public static final QAddr addr = new QAddr("addr");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath addrName = createString("addrName");

    public final NumberPath<Integer> addrNo = createNumber("addrNo", Integer.class);

    public final StringPath defaultAddr = createString("defaultAddr");

    public final StringPath hp = createString("hp");

    public final StringPath receiver = createString("receiver");

    public final StringPath userId = createString("userId");

    public final NumberPath<Integer> zip = createNumber("zip", Integer.class);

    public QAddr(String variable) {
        super(Addr.class, forVariable(variable));
    }

    public QAddr(Path<? extends Addr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddr(PathMetadata metadata) {
        super(Addr.class, metadata);
    }

}

