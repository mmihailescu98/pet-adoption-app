import tsParser from '@typescript-eslint/parser';

export default [
  {
    files: ['**/*.ts'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: 'module',
      },
    },
    rules: {
      'no-console': ['error', { allow: ['warn', 'error'] }]
    }
  },
  {
    ignores: ['src/app/api/**/*']
  }
];
