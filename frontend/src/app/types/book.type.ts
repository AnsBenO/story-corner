// get book by code reponse

export interface Book {
  id: number;
  code: string;
  name: string;
  description: string;
  imageUrl: string;
  price: number;
  createdAt: Date;
  updatedAt: Date;
}
