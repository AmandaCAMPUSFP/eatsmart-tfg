export interface Meal {
  id: string;
  name: string;
  description: string;
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
  category: string;
  image?: string;
  createdAt: Date;
}

export interface MealLog {
  id: string;
  userId: string;
  mealId: string;
  date: Date;
  quantity: number;
}
