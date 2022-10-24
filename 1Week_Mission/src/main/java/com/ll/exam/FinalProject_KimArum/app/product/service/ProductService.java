package com.ll.exam.FinalProject_KimArum.app.product.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostHashTag;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostHashTagService;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostKeywordService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.entity.ProductTag;
import com.ll.exam.FinalProject_KimArum.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;
    private final PostHashTagService postHashTagService;
    private final ProductTagService productTagService;

    @Transactional
    public Product create(Member author, String subject, int price, long postKeywordId, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findById(postKeywordId).get();

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, String postKeywordContent, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findByContentOrSave(postKeywordContent);

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, PostKeyword postKeyword, String productTagContents) {
        Product product = Product
                .builder()
                .author(author)
                .postKeyword(postKeyword)
                .subject(subject)
                .price(price)
                .build();

        productRepository.save(product);

        applyProductTags(product, productTagContents);

        return product;
    }

    @Transactional
    public void applyProductTags(Product product, String productTagContents) {
        productTagService.applyProductTags(product, productTagContents);
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findForPrintById(long id) {
        Optional<Product> opProduct = findById(id);

        if (opProduct.isEmpty()) return opProduct;

        List<ProductTag> productTags = getProductTags(opProduct.get());

        opProduct.get().getExtra().put("productTags", productTags);

        return opProduct;
    }

    private List<ProductTag> getProductTags(Product product) {
        return productTagService.getProductTags(product);
    }

    public List<Post> findPostsByProduct(Product product) {
        Member author = product.getAuthor();
        PostKeyword postKeyword = product.getPostKeyword();
        List<PostHashTag> postTags = postHashTagService.getPostTags(author.getId(), postKeyword.getId());

        return postTags
                .stream()
                .map(PostHashTag::getPost)
                .collect(toList());
    }

    public List<Product> search(String kwType, String kw) {
        return productRepository.searchQsl(kwType, kw);
    }

    public void loadForPrintData(List<Product> products) {
        long[] ids = products
                .stream()
                .mapToLong(Product::getId)
                .toArray();

        List<ProductTag> productTagsByProductIds = productTagService.getProductTagsByProductIdIn(ids);

        Map<Long, List<ProductTag>> productTagsByProductIdMap = productTagsByProductIds.stream()
                .collect(groupingBy(
                        productTag -> productTag.getProduct().getId(), toList()
                ));

        products.stream().forEach(product -> {
            List<ProductTag> productTags = productTagsByProductIdMap.get(product.getId());

            if (productTags == null || productTags.size() == 0) return;

            product.getExtra().put("productTags", productTags);
        });
    }
}
