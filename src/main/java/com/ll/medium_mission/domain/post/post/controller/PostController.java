package com.ll.medium_mission.domain.post.post.controller;

import com.ll.medium_mission.domain.post.post.entity.Post;
import com.ll.medium_mission.domain.post.post.service.PostService;
import com.ll.medium_mission.global.rq.Rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id) {
        rq.setAttribute("post", postService.findById(id).get());

        return "domain/post/post/detail";
    }

    @GetMapping("/list")
    public String showList(
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Page<Post> postPage = postService.search(kw, pageable);
        rq.setAttribute("postPage", postPage);
        rq.setAttribute("page", page);

        return "domain/post/post/list";
    }

    @GetMapping("/write")
    public String postWrite() {
        return "domain/post/post/write";
    }

    @PostMapping("/write")
    public String postWrite(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
        // TODO 질문을 저장한다.
        this.postService.create(subject, content);
        return "redirect:list"; // 질문 저장후 질문목록으로 이동
    }
}