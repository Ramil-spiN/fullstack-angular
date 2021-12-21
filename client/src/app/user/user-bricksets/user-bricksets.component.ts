import {Component, OnInit} from '@angular/core';
import {Brickset} from "../../model/Brickset";
import {BricksetService} from "../../service/brickset.service";
import {ImageService} from "../../service/image.service";
import {CommentService} from "../../service/comment.service";
import {NotificationService} from "../../service/notification.service";

@Component({
  selector: 'app-user-bricksets',
  templateUrl: './user-bricksets.component.html',
  styleUrls: ['./user-bricksets.component.css']
})
export class UserBricksetsComponent implements OnInit {

  isUserBricksetsLoaded = false;
  bricksets: Brickset[];

  constructor(private bricksetService: BricksetService,
              private imageService: ImageService,
              private commentService: CommentService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.bricksetService.getBricksetsForCurrentUser()
      .subscribe(data => {
        this.bricksets = data;
        this.getImagesToBricksets(this.bricksets);
        this.getCommentsToBricksets(this.bricksets);
        this.isUserBricksetsLoaded = true;
      })
  }

  getImagesToBricksets(bricksets: Brickset[]): void {
    bricksets.forEach(b => {
      this.imageService.getBricksetImage(b.id!)
        .subscribe(data => {
          b.image = data.imageBytes;
        });
    });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  getCommentsToBricksets(bricksets: Brickset[]): void {
    bricksets.forEach(b => {
      this.commentService.getCommentsForBrickset(b.id!)
        .subscribe(data => {
          b.comments = data;
        });
    });
  }

  deleteComment(commentId: number, bricksetIndex: number, commentIndex: number): void {
    const brickset = this.bricksets[bricksetIndex];

    this.commentService.deleteComment(commentId)
      .subscribe(() => {
        this.notificationService.showSnackBar('Comment removed');
        brickset.comments!.splice(commentIndex, 1);
      });
  }

  removeBrickset(brickset: Brickset, index: number) {
    console.log(brickset);
    const result = confirm('Do you really want to delete this Brickset?');
    if (result) {
      this.bricksetService.deleteBrickset(brickset.id!)
        .subscribe(() => {
          this.bricksets.splice(index, 1);
          this.notificationService.showSnackBar('Brickset deleted');
        });
    }

  }
}
