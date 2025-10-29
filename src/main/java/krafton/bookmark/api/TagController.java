package krafton.bookmark.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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

@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "태그 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "모든 태그 조회", description = "현재 사용자의 모든 태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "태그 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = TagResponse.class)))
    @GetMapping
    public ResponseEntity<?> getAll(@Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails){

        List<TagResponse> result = tagService.findAll(userDetails.getMember());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "태그 생성 성공",
            content = @Content(schema = @Schema(implementation = Tag.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody TagSaveApiRequest req,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails){

        Tag save = tagService.save(new TagSaveRequest(
                req.tagName(), userDetails.getMember()
        ));

        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @Operation(summary = "단일 태그 조회", description = "특정 ID의 태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "태그 조회 성공",
            content = @Content(schema = @Schema(implementation = TagResponse.class)))
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(
            @Parameter(description = "태그 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails){

        TagResponse find = tagService.findOne(userDetails.getMember(), id);

        return new ResponseEntity<>(find, HttpStatus.OK);
    }

    @Operation(summary = "태그 수정", description = "특정 ID의 태그 이름을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "태그 수정 성공",
            content = @Content(schema = @Schema(implementation = TagResponse.class)))
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "태그 ID") @PathVariable Long id,
            @Valid @RequestBody TagUpdateApiRequest req,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails){

        TagResponse result = tagService.updateName(new TagUpdateRequest(
                id, userDetails.getMember(), req.updateName()
        ));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "태그 삭제", description = "특정 ID의 태그를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "태그 삭제 성공")
    @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "태그 ID") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        tagService.delete(userDetails.getMember(), id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
