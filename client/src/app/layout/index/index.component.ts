import {Component, OnInit} from '@angular/core';
import {Brickset} from "../../model/Brickset";
import {User} from "../../model/User";
import {BricksetService} from "../../service/brickset.service";
import {UserService} from "../../service/user.service";
import {CommentService} from "../../service/comment.service";
import {ImageService} from "../../service/image.service";
import {NotificationService} from "../../service/notification.service";

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  isUserDataLoaded = false;
  user: User;
  isBricksetsLoaded = false;
  bricksets: Brickset[];

  constructor(private userService: UserService,
              private bricksetService: BricksetService,
              private commentService: CommentService,
              private imageService: ImageService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
    this.bricksetService.getAllBricksets()
      .subscribe(data => {
        console.log(data);
        this.bricksets = data;
        this.getImagesToBricksets(this.bricksets);
        this.getCommentsToBricksets(this.bricksets);
        this.isBricksetsLoaded = true;
      });

    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
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

  getCommentsToBricksets(bricksets: Brickset[]): void {
    bricksets.forEach(b => {
      this.commentService.getCommentsForBrickset(b.id!)
        .subscribe(data => {
          b.comments = data;
        });
    });
  }

  likeBrickset(bricksetId: number, bricksetIndex: number): void {
    const brickset = this.bricksets[bricksetIndex];

    if (!brickset.likedUsers?.includes(this.user.username)) {
      this.bricksetService.likeBrickset(bricksetId, this.user.username)
        .subscribe(() => {
          brickset.likedUsers?.push(this.user.username);
          this.notificationService.showSnackBar('Liked');
        });
    } else {
      this.bricksetService.likeBrickset(bricksetId, this.user.username)
        .subscribe(() => {
          const index = brickset.likedUsers?.indexOf(this.user.username, 0);
          if (index! > -1) {
            brickset.likedUsers?.splice(index!, 1);
          }
        });
    }
  }

  commentBrickset(message: string, bricksetId: number, bricksetIndex: number): void {
    const brickset = this.bricksets[bricksetIndex];

    console.log(brickset);
    this.commentService.createComment(bricksetId, message)
      .subscribe(data => {
        console.log(data);
        brickset.comments?.push(data);
      });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

}
