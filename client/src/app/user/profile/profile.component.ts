import {Component, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {UserService} from "../../service/user.service";
import {BricksetService} from "../../service/brickset.service";
import {Token} from "@angular/compiler";
import {TokenStorageService} from "../../service/token-storage.service";
import {ImageService} from "../../service/image.service";
import {NotificationService} from "../../service/notification.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {EditUserComponent} from "../edit-user/edit-user.component";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  isUserDataLoaded = false;
  user: User;
  selectedFile: File;
  userProfileImage: File;
  previewImageURL: any;

  constructor(private userService: UserService,
              private tokenService: TokenStorageService,
              private bricksetService: BricksetService,
              private imageService: ImageService,
              private notificationService: NotificationService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.userService.getCurrentUser()
      .subscribe(data => {
        this.user = data;
        this.isUserDataLoaded = true;
      });

    this.imageService.getProfileImage()
      .subscribe(data => {
        this.userProfileImage = data.imageBytes;
      });
  }

  onFileSelected(event): void {
    this.selectedFile = event.target.files[0];

    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    reader.onload = () => {
      this.previewImageURL = reader.result;
    }
  }

  openEditDialog(): void {
    const dialogUserEditConfig = new MatDialogConfig();
    dialogUserEditConfig.width = '400px';
    dialogUserEditConfig.data = {
      user: this.user
    }
    this.dialog.open(EditUserComponent, dialogUserEditConfig);
  }

  onUpload(): void {
    if (this.selectedFile != null) {
      this.imageService.uploadProfileImage(this.selectedFile)
        .subscribe(() => {
          this.notificationService.showSnackBar("Profile image uploaded successfully");
        });
    }
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }
}
