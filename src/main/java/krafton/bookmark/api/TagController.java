package krafton.bookmark.api;

import krafton.bookmark.api.dto.TagSaveApiRequest;
import krafton.bookmark.api.dto.TagUpdateApiRequest;
import krafton.bookmark.application.dto.TagResponse;
import krafton.bookmark.application.dto.TagSaveRequest;
import krafton.bookmark.application.dto.TagUpdateRequest;
import krafton.bookmark.application.service.TagService;
import krafton.bookmark.common.security.details.CustomUserDetails;
import krafton.bookmark.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal CustomUserDetails userDetails){

        List<TagResponse> result = tagService.findAll(userDetails.getMember());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody TagSaveApiRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        Tag save = tagService.save(new TagSaveRequest(
                req.tagName(), userDetails.getMember()
        ));

        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        TagResponse find = tagService.findOne(userDetails.getMember(), id);

        return new ResponseEntity<>(find, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody TagUpdateApiRequest req,
            @AuthenticationPrincipal CustomUserDetails userDetails){

        TagResponse result = tagService.updateName(new TagUpdateRequest(
                id, userDetails.getMember(), req.updateName()
        ));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        tagService.delete(userDetails.getMember(), id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
