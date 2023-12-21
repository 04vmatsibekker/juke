import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:3000/');
  await page.locator('#inputName').click();
  await page.locator('#inputName').fill('Evan');
  await page.getByRole('button', { name: 'Submit' }).click();
 await page.waitForTimeout(500);
 await expect( page.locator('#response')).toHaveText('Hello, Evan!');
  await page.locator('#inputName').fill('Pavel');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.waitForTimeout(500);
  await expect( page.locator('#response')).toHaveText('Hello, Pavel!');
});