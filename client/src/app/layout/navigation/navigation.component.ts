import {Component, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {TokenStorageService} from "../../service/token-storage.service";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {ImageService} from "../../service/image.service";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  isLoggedIn = false;
  isDataLoaded = false;
  isImageLoaded = false;
  user: User;

  constructor(private tokenService: TokenStorageService,
              private userService: UserService,
              private imageService: ImageService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();

    if (this.isLoggedIn) {
      this.userService.getCurrentUser()
        .subscribe(data => {
          console.log(data);
          this.user = data;
          this.getImageToProfile(this.user);
          this.isDataLoaded = true;
        })
    }
  }

  logout(): void {
    this.tokenService.logOut();
    this.router.navigate(['/login']);
  }

  getImageToProfile(user): void {
    this.imageService.getProfileImage()
      .subscribe(data => {
        if (data.name != null) {
          this.isImageLoaded = true;
        }
        user.image = data.imageBytes;
      });
  }

  formatImage(img: any): any {
    if (img == null) {
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

}
