import { SpaceToUnderscorePipe } from './space-to-underscore.pipe';

describe('SpaceToUnderscorePipe', () => {
  it('create an instance', () => {
    const pipe = new SpaceToUnderscorePipe();
    expect(pipe).toBeTruthy();
  });
});
