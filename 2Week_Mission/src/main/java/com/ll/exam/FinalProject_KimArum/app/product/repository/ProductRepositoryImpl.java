package com.ll.exam.FinalProject_KimArum.app.product.repository;

import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.util.Util;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.FinalProject_KimArum.app.order.entity.QOrderItem.orderItem;
import static com.ll.exam.FinalProject_KimArum.app.product.entity.QProduct.product;
import static com.ll.exam.FinalProject_KimArum.app.product.entity.QProductKeyword.productKeyword;
import static com.ll.exam.FinalProject_KimArum.app.product.entity.QProductTag.productTag;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Product> searchQsl(String kwType, String kw) {
        JPAQuery<Product> jpqQuery = jpaQueryFactory
                .select(product)
                .distinct()
                .from(product);

        // 키워드가 존재하고
        if (Util.str.empty(kw) == false) {
            // 키워드 타입이 해시태그이다.
            if (Util.str.eq(kwType, "hashTag")) {
                jpqQuery
                        .innerJoin(productTag)
                        .on(product.eq(productTag.product))
                        .innerJoin(productKeyword)
                        .on(productKeyword.eq(productTag.productKeyword))
                        .where(productKeyword.content.eq(kw));

            }
        }

        jpqQuery.orderBy(product.id.desc());

        return jpqQuery.fetch();
    }

    @Override
    public List<Product> findProductByOrderId(Long orderId) {
        JPAQuery<Product> jpqQuery = jpaQueryFactory
                .select(product)
                .distinct()
                .from(product)
                .innerJoin(orderItem)
                .on(product.id.eq(orderItem.product.id))
                .where(orderItem.order.id.eq(orderId));

        jpqQuery.orderBy(orderItem.order.id.desc());

        return jpqQuery.fetch();
    }
}
