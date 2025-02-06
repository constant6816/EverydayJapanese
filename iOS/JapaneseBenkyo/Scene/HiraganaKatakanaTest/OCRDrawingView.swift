//
//  DrawingView.swift
//  OCRSample
//
//  Created by 김호성 on 2024.11.15.
//

import UIKit
import Combine

class OCRDrawingView: DrawingView {
    
    private var cancellable: Set<AnyCancellable> = Set<AnyCancellable>()
    private var ocrEvent: PassthroughSubject<Void, Never> = PassthroughSubject<Void, Never>()
    private let hiraganaKatakanaSample: UIImage = UIImage(resource: .hksamplew).withRenderingMode(.alwaysOriginal).withTintColor(.label)
    
    weak var delegate: DrawingViewDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        bindUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        bindUI()
    }
    
    private func bindUI() {
        ocrEvent
            .debounce(for: .seconds(0.5), scheduler: RunLoop.main)
            .sink(receiveValue: { [weak self] in
                self?.delegate?.willExtractText()
                self?.requestExtractText()
            })
            .store(in: &cancellable)
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesBegan(touches, with: event)
        ocrEvent.send(())
    }
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        super.touchesMoved(touches, with: event)
        ocrEvent.send(())
    }
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        ocrEvent.send(())
    }
    
    private func requestExtractText() {
        createDrawingImageAsync()
            .flatMap({ image in
                return OCRManager.shared.extractText(from: image ?? UIImage())
            })
            .subscribe(on: DispatchQueue.global())
            .receive(on: DispatchQueue.main)
            .sink(receiveValue: { [weak self] value in
                self?.delegate?.didExtractText(text: value)
            })
            .store(in: &cancellable)
    }
    
    override func undo() {
        _ = lines.popLast()
        setNeedsDisplay()
        self.delegate?.didExtractText(text: nil)
    }
    override func clear() {
        lines.removeAll()
        setNeedsDisplay()
        self.delegate?.didExtractText(text: nil)
    }
    
    private func toProcessedImage() -> UIImage? {
        let width = max(self.bounds.size.width, hiraganaKatakanaSample.size.width)
        let height = self.bounds.size.height + hiraganaKatakanaSample.size.height
        let size = CGSize(width: width, height: height)
        
        let renderer = UIGraphicsImageRenderer(size: size)
        
        return renderer.image { context in
            layer.render(in: context.cgContext)
            hiraganaKatakanaSample.draw(in: CGRect(x: 0, y: self.bounds.size.height, width: hiraganaKatakanaSample.size.width, height: hiraganaKatakanaSample.size.height))
        }
    }
    
    func toImage() -> UIImage? {
        let renderer = UIGraphicsImageRenderer(bounds: bounds)
        return renderer.image { context in
            layer.render(in: context.cgContext)
        }
    }
    
    private func createDrawingImageAsync() -> Future<UIImage?, Never> {
        return Future { [weak self] promise in
            guard let self = self else { return promise(.success(nil)) }
            return promise(.success(self.toProcessedImage()))
        }
    }
}

protocol DrawingViewDelegate: AnyObject {
    func willExtractText()
    func didExtractText(text: String?)
}
