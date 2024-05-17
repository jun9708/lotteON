package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 901081966L;

    public static final QUser user = new QUser("user");

    public final StringPath userAddr1 = createString("userAddr1");

    public final StringPath userAddr2 = createString("userAddr2");

    public final DatePath<java.time.LocalDate> userBirth = createDate("userBirth", java.time.LocalDate.class);

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userGender = createString("userGender");

    public final StringPath userGrade = createString("userGrade");

    public final StringPath userHp = createString("userHp");

    public final StringPath userId = createString("userId");

    public final StringPath userName = createString("userName");

    public final StringPath userProfile = createString("userProfile");

    public final StringPath userPromo = createString("userPromo");

    public final StringPath userProvider = createString("userProvider");

    public final StringPath userPw = createString("userPw");

    public final DateTimePath<java.time.LocalDateTime> userRegDate = createDateTime("userRegDate", java.time.LocalDateTime.class);

    public final StringPath userRole = createString("userRole");

    public final StringPath userStatus = createString("userStatus");

    public final DateTimePath<java.time.LocalDateTime> userUpdate = createDateTime("userUpdate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> userVisitDate = createDateTime("userVisitDate", java.time.LocalDateTime.class);

    public final StringPath userZip = createString("userZip");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

