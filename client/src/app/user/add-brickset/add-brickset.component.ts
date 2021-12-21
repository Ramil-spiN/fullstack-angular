import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Brickset} from "../../model/Brickset";
import {BricksetService} from "../../service/brickset.service";
import {ImageService} from "../../service/image.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-brickset',
  templateUrl: './add-brickset.component.html',
  styleUrls: ['./add-brickset.component.css']
})
export class AddBricksetComponent implements OnInit {

  bricksetForm: FormGroup;
  selectedFile: File;
  isBricksetCreated = false;
  createdBrickset: Brickset;
  previewImgURL: any;

  constructor(private bricksetService: BricksetService,
              private imageService: ImageService,
              private notificationService: NotificationService,
              private router: Router,
              private fb: FormBuilder) { }

  ngOnInit(): void {
    this.bricksetForm = this.createBricksetForm()
  }

  createBricksetForm(): FormGroup {
    return this.fb.group({
      title: ['', Validators.compose([Validators.required])],
      caption: ['', Validators.compose([Validators.required])],
      inventory: ['', Validators.compose([Validators.required])],
    });
  }

  submit(): void {
    this.bricksetService.createBrickset({
      title: this.bricksetForm.value.title,
      caption: this.bricksetForm.value.caption,
      inventory: this.bricksetForm.value.inventory,
    }).subscribe(data => {
      this.createdBrickset = data;
      console.log(data);

      if (this.createdBrickset.id != null) {
        this.imageService.uploadBricksetImage(this.createdBrickset.id, this.selectedFile)
          .subscribe(() => {
            this.notificationService.showSnackBar('Brickset added successfully');
            this.isBricksetCreated = true;
            this.router.navigate(['/profile']);
          });
      }
    });
  }

  onFileSelected(event): void {
    this.selectedFile = event.target.files[0];

    const reader = new FileReader();
    reader.readAsDataURL(this.selectedFile);
    reader.onload = (e) => {
      this.previewImgURL = reader.result;
    };
  }

}
