# MDD Front-end

Front-end application for the MDD (Monde de Dév) social network, built with Angular.

## Technologies

- **Angular**: 14.1.0
- **Angular CLI**: 14.2.13
- **TypeScript**: 4.7.2
- **RxJS**: 7.5.0
- **Angular Material**: 14.2.5
- **SCSS**: CSS preprocessor

## Prerequisites

- Node.js 14.x or higher
- npm 6.x or higher

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/your-username/mdd-project.git
cd mdd-project/front
```

### 2. Install dependencies

```bash
npm install
```

### 3. Configure the API URL

Update the API URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

### 4. Run the application

```bash
npm start
```

The application will be available at `http://localhost:4200`

## Available Scripts

- `npm start` - Start development server
- `npm run build` - Build for production
- `npm test` - Run unit tests
- `npm run lint` - Run linter

## Features

### Authentication
- User registration with password validation
- User login (email or username)
- Persistent session
- Automatic logout

### Posts Feed
- Display posts from subscribed topics
- Sort by date (newest/oldest)
- View post details
- Create new posts
- Add comments

### Topics
- Browse all available topics
- Subscribe/unsubscribe to topics
- Visual feedback for subscribed topics

### User Profile
- View profile information
- Update email, username, and password
- View subscribed topics
- Unsubscribe from topics

## Project Structure

```
front/
├── src/
│   ├── app/
│   │   ├── features/           # Feature modules
│   │   │   ├── auth/          # Authentication
│   │   │   ├── posts/         # Posts management
│   │   │   ├── topics/        # Topics management
│   │   │   └── profile/       # User profile
│   │   ├── services/          # Services (API calls)
│   │   ├── guards/            # Route guards
│   │   ├── interfaces/        # TypeScript interfaces
│   │   ├── interceptors/      # HTTP interceptors
│   │   └── header/            # Header components
│   └── environments/          # Environment configuration
```

## Architecture

- **Modular architecture**: Feature modules for better organization
- **Reactive programming**: RxJS Observables for async operations
- **Route guards**: Authentication and authorization protection
- **Interceptors**: Centralized HTTP error handling
- **Services**: Separation of business logic from components
- **TypeScript interfaces**: Strong typing for all entities

## Responsive Design

The application is fully responsive with breakpoints at:
- Desktop: > 768px
- Mobile: ≤ 768px

## Security

- Route guards (AuthGuard, UnauthGuard, PostAccessGuard)
- Client-side form validation
- Secure cookie-based authentication
- XSS protection via Angular sanitization
