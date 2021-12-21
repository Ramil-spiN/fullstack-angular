import {Comment} from './Comment'

export interface Brickset {
  id?: number;
  title: string;
  caption: string;
  inventory: string;
  image?: File;
  likes?: number;
  likedUsers?: string[];
  comments?: Comment[];
  username?: string;
  userImage?: File;
  isUserImageLoaded?: boolean;
}
